package com.yc.feed.web.controller;

import com.yc.feed.api.http.req.WebAddCommentReq;
import com.yc.feed.api.http.req.WebOrderInfo;
import com.yc.feed.api.http.res.*;
import com.yc.feed.api.util.FeedStringUtil;
import com.yc.feed.domain.entity.OrderComment;
import com.yc.feed.domain.entity.OrderCommentTag;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.CommentTagModel;
import com.yc.feed.domain.model.OrderCommentInfo;
import com.yc.feed.domain.model.OrderInfo;
import com.yc.feed.domain.req.AddCommentReq;
import com.yc.feed.domain.req.ChangeCommentStatusReq;
import com.yc.feed.domain.req.GetCommentListReq;
import com.yc.feed.lock.Lock;
import com.yc.feed.service.EvaluateHttpService;
import com.yc.feed.service.LockService;
import com.yc.feed.service.TagService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yusong on 2016/10/13.
 * 用户评论控制器
 */
@Controller
@RequestMapping("/feed")
public class OrderCommentCtrl {

    private final Logger logger = Logger.getLogger(OrderCommentCtrl.class);
    @Autowired
    private EvaluateHttpService evaluateHttpService;
    @Autowired
    private LockService lockService;

    /*
    *添加用户评论
    */
    @RequestMapping("/addOrderComment")
    @ResponseBody
    public AddCommentRes addUserComment(@RequestBody WebAddCommentReq addCommentReq){
        long startTime =  System.currentTimeMillis();
        try {
            logger.info("addUserComment|getInfo:"+addCommentReq);
            AddCommentReq domainReq = null;
            try {
                domainReq = parsePara(addCommentReq);
            } catch (InvalidParameterException e) {
                logger.error("addUserComment|参数校验不通过|"+addCommentReq,e);
                return new  AddCommentRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), e.getMessage());
            }
            AddCommentRes res = null;
            Lock redisLock = lockService.getLock(domainReq.getOrderId()+"");
            if(redisLock.tryLock(200)){
                try {
                    res = evaluateHttpService.addUserComment(domainReq);
                } catch (FeedException e) {
                    logger.error("addUserComment|添加用户评论错误|",e);
                    res = new AddCommentRes(Boolean.FALSE, e.getCode(), e.getMsg());
                }finally {
                    redisLock.unlock();
                    //释放map的key
                    lockService.removeLock(domainReq.getOrderId()+"");
                }
            }

            logger.info("addUserComment|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("addUserComment|程序异常|:"+e.getMessage());
        }
        return new AddCommentRes(Boolean.FALSE,CodeTypes.SystemError.getCode(),"新增订单评论异常");
    }

    /*
    *获取订单评论
    */
    @RequestMapping("/getOrderComment")
    @ResponseBody
    public GetOrderCommentRes getOrderComment(HttpServletRequest request){
        try{
            long startTime =  System.currentTimeMillis();
            String orderId = request.getParameter("orderId");
            logger.info("getOrderComment|获取用户评价|orderId:"+orderId);
            if(StringUtils.isEmpty(orderId)){
                return new GetOrderCommentRes(Boolean.FALSE,String.valueOf(CodeTypes.ParaError.getCode()),"订单号不能为空",null);
            }else if (!FeedStringUtil.isLong(orderId)){
                return new GetOrderCommentRes(Boolean.FALSE,String.valueOf(CodeTypes.ParaError.getCode()),"订单号格式必须为数字",null);
            }
            OrderCommentInfo orderComment;
            Long orderIdNum = Long.parseLong(orderId);
            try {
                orderComment = evaluateHttpService.getByOrderId(orderIdNum);
            } catch (FeedException e) {
                return new GetOrderCommentRes(Boolean.FALSE,String.valueOf(e.getCode()),e.getMsg(),null);
            }
            if (null == orderComment){
                logger.warn("getOrderComment|未查询到记录|orderId:"+orderId);
                return new GetOrderCommentRes(Boolean.FALSE,String.valueOf(CodeTypes.NoRecord.getCode()),CodeTypes.NoRecord.getMsg(),null);
            }

            GetOrderCommentRes res = new GetOrderCommentRes(Boolean.TRUE,String.valueOf(CodeTypes.Success.getCode()),"查询订单评价成功",orderComment);
            logger.info("getOrderComment|程序响应时间：|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("getOrderComment|程序异常|:"+e.getMessage());
        }
        return  new GetOrderCommentRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"程序异常",null);
    }

    /*
    *获取评论列表
    */
    @RequestMapping("/getOrderCommentList")
    @ResponseBody
    public GetCommentListRes getOrderCommentList(HttpServletRequest request){
        long startTime =  System.currentTimeMillis();
        try{
            GetCommentListReq req = null;
            try {
                req = parseListPara(request);
            } catch (InvalidParameterException e) {
                logger.info("getOrderCommentList|参数错误|", e);
                return new GetCommentListRes(Boolean.FALSE,String.valueOf(CodeTypes.ParaError.getCode()),e.getMessage());
            }
            List<OrderCommentInfo> orderComments = evaluateHttpService.getCommentList(req);

            long total = 0;
            if(req.getOrderIds()!=null){
                total = orderComments.size();
            }else{
                total = evaluateHttpService.getCount(req);
            }

            GetCommentListRes res = new GetCommentListRes(Boolean.TRUE,String.valueOf(CodeTypes.Success.getCode()),"success",total,orderComments.size(),orderComments);
            logger.info("getOrderCommentList|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("getOrderCommentList|程序异常|:"+e.getMessage());
        }
        return  new GetCommentListRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"程序异常");
    }
    
    /*
    *星火项目通过差评订单列表获取差评标签和订单时间
    *orderIds订单列表
    *tagFlag 返回好评标签：1   返回差评标签：-1  返回所有标签：默认|不传参
    */
    @RequestMapping("/getOrderCommentList/byOrderIds")
    @ResponseBody
    public GetOrderCommentListByIdsRes getOrderCommentListByIds(HttpServletRequest request){
        long startTime =  System.currentTimeMillis();
    	String orderIds = request.getParameter("orderIds");
    	String tagFlag = request.getParameter("tagFlag");

    	try {
    		StringBuilder errorString = new StringBuilder();
        	if (orderIds == null || orderIds.length() == 0) {
        		return new GetOrderCommentListByIdsRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),"OrderIds is a must Parameter");
        	}
        	if (orderIds != null && orderIds.length() > 0) {
        		String[] ids = orderIds.split(",");
      	        for (String id:ids) {
      	        	  if(!FeedStringUtil.isLong(id)) {
      	        		  errorString.append("orderId must be number | ");
      	              }
      	        }
        	}
        	if (tagFlag != null) {
        		if (!FeedStringUtil.isInteger(tagFlag)) {
        			errorString.append("tagFlag must be number");
        		}
        	}
        	
        	if (errorString.length() > 0) {
                  return new GetOrderCommentListByIdsRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),errorString.toString());
        	}
        	
        	if (tagFlag == null) {
        		tagFlag = "0";
        	}
	        List<OrderCommentInfo> orderComments = evaluateHttpService.getCommentListByIds(orderIds, Integer.valueOf(tagFlag));
	        
	        GetOrderCommentListByIdsRes res = new GetOrderCommentListByIdsRes(Boolean.TRUE,String.valueOf(CodeTypes.Success.getCode()),"success",orderComments);
            logger.info("getOrderCommentListByIds|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
	        return res;
    	} catch (Exception e) {
    		logger.error("OrderCommentCtrl.getOrderCommentListByIds throw an error." + e);
            return new GetOrderCommentListByIdsRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()), "fail");
    	} 
    }

    /*
    *  更新展示状态(fro ERP)
    */
    @RequestMapping(value = "/updateDisplayStatus", method = RequestMethod.GET)
    @ResponseBody
    public CommonRes updateDisplayStatus(HttpServletRequest request){
        long startTime =  System.currentTimeMillis();
        String orderId = request.getParameter("orderId");
        String newStatus = request.getParameter("newStatus");
        String operatorId = request.getParameter("operatorId");

        try {
            CommonRes res = null;
            StringBuilder errorString = new StringBuilder();
            if (orderId == null || !FeedStringUtil.isLong(orderId)) {
                errorString.append("OrderId must be number|");
            }

            if (newStatus == null || !FeedStringUtil.isInteger(newStatus)) {
                errorString.append("newStatus must be number|");
            }

            if (operatorId == null || !FeedStringUtil.isInteger(operatorId)) {
                errorString.append("operatorId must be number");
            }

            if (errorString.length() > 0) {
                return new CommonRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),errorString.toString());
            }
            try {
                evaluateHttpService.changeDisplayStatus(Long.valueOf(orderId), Integer.valueOf(newStatus), Integer.valueOf(operatorId));
                res = new CommonRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),"success");
            } catch (FeedException e) {
                res = new  CommonRes(Boolean.FALSE, String.valueOf(e.getCode()),e.getMsg());
            }
            logger.info("updateDisplayStatus|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        } catch (Exception e) {
            logger.error("OrderCommentCtrl.updateDisplayStatus throw an error." + e);
            return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()), "fail");
        }
    }

    private GetCommentListReq parseListPara(HttpServletRequest request)throws InvalidParameterException{
        GetCommentListReq req = new GetCommentListReq();
        String driverId = request.getParameter("driverId");
        String userId = request.getParameter("userId");
        String evaluation = request.getParameter("evaluation");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");
        String needTag = request.getParameter("needTag");
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        //时间格式改为时间戳
        String startTimeStamp = request.getParameter("startTimeStamp");
        String endTimeStamp = request.getParameter("endTimeStamp");

        //追加项目 满足erp需求
        String content = request.getParameter("content");
        String city = request.getParameter("city");
        String carTypeId = request.getParameter("carTypeId");
        String orderIds = request.getParameter("orderIds");
        String tagIdStr = request.getParameter("tagId");

        //老乘客端需求
        String status = request.getParameter("status");
        String displayStatus = request.getParameter("displayStatus");
        String descFlag = request.getParameter("descFlag");

        boolean hasNoParaFlag = true;

        if(!StringUtils.isEmpty(descFlag)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isBoolean(descFlag)){
                throw new InvalidParameterException("descFlag格式必须布尔型。");
            }else{
                req.setDescFlag(Boolean.parseBoolean(descFlag));
            }
        }else{
            req.setDescFlag(Boolean.FALSE);
        }

        if(!StringUtils.isEmpty(displayStatus)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isInteger(displayStatus)){
                throw new InvalidParameterException("displayStatus格式必须为数字。");
            }else{
                req.setDisplayStatus(Integer.parseInt(displayStatus));
            }
        }

        if(!StringUtils.isEmpty(status)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isInteger(status)){
                throw new InvalidParameterException("status格式必须为数字。");
            }else{
                req.setStatus(Integer.parseInt(status));
            }
        }

        if(!StringUtils.isEmpty(userId)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isLong(userId)){
                throw new InvalidParameterException("用户ID格式必须为数字。");
            }else{
                req.setUserId(Long.parseLong(userId));
            }
        }

        if(!StringUtils.isEmpty(driverId)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isLong(driverId)){
                throw new InvalidParameterException("司机ID格式必须为数字。");
            }else{
                req.setDriverId(Long.parseLong(driverId));
            }
        }


        if(!StringUtils.isEmpty(startTimeStamp)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isLong(startTimeStamp)){
                throw new InvalidParameterException("startTimeStamp格式必须为数字。");
            }else{
                req.setStartTimeStamp(Long.parseLong(startTimeStamp));
            }
        }

        if(!StringUtils.isEmpty(endTimeStamp)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isLong(endTimeStamp)){
                throw new InvalidParameterException("endTimeStamp格式必须为数字。");
            }else{
                req.setEndTimeStamp(Long.parseLong(endTimeStamp));
            }
        }

        if(!StringUtils.isEmpty(evaluation)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isInteger(evaluation)){
                throw new InvalidParameterException("evaluation格式必须为数字。");
            }else{
                req.setEvaluation(Integer.parseInt(evaluation));
            }
        }


        //分页参数
        if(!StringUtils.isEmpty(pageNum)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isLong(pageNum)){
                throw new InvalidParameterException("pageNum格式必须为数字。");
            }else{
                req.setPageNum(Long.parseLong(pageNum));
            }
        }

        if(!StringUtils.isEmpty(pageSize)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isLong(pageSize)){
                throw new InvalidParameterException("pageSize格式必须为数字。");
            }else{
                req.setPageSize(Long.parseLong(pageSize));
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(!StringUtils.isEmpty(startTimeStr)){
            hasNoParaFlag = false;
            try {
                Date startTime = sdf.parse(startTimeStr);
                req.setStartTime(startTime);
            } catch (ParseException e) {
                throw new InvalidParameterException("startTime格式为yyyy-MM-dd HH:mm:ss");
            }
        }

        if(!StringUtils.isEmpty(endTimeStr)){
            hasNoParaFlag = false;
            try {
                Date endTime = sdf.parse(endTimeStr);
                req.setEndTime(endTime);
            } catch (ParseException e) {
                throw new InvalidParameterException("endTime格式为yyyy-MM-dd HH:mm:ss");
            }
        }

        if(!StringUtils.isEmpty(needTag)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isBoolean(needTag)){
                throw new InvalidParameterException("needTag格式必须布尔型。");
            }else{
                req.setNeedTag(Boolean.parseBoolean(needTag));
            }
        }else{
            req.setNeedTag(false);
        }

        if(!StringUtils.isEmpty(carTypeId)){
            hasNoParaFlag = false;
            if(!FeedStringUtil.isInteger(carTypeId)){
                throw new InvalidParameterException("carTypeId格式必须为数字。");
            }else{
                req.setCarTypeId(Integer.parseInt(carTypeId));
            }
        }
        //erp的模糊查询
        if (content != null && content.length() > 0) {
            hasNoParaFlag = false;
            req.setContent("%"+content+"%");
        }
        if (city != null && city.length() > 0) {
            hasNoParaFlag = false;
            req.setCity(city);
        }


        if (!StringUtils.isEmpty(orderIds)) {
            hasNoParaFlag = false;
            String[] strOrderIds = orderIds.split(",");
            for (String str : strOrderIds) {
                if (!FeedStringUtil.isLong(str)) {
                    throw new InvalidParameterException("orderIds的订单id必须为数字");
                }
            }
            req.setOrderIds(orderIds);
        }

        if(!StringUtils.isEmpty(tagIdStr)){
            if(!FeedStringUtil.isInteger(tagIdStr)){
                throw new InvalidParameterException("标签Id格式必须为数字。");
            }else{
                req.setTagId(Integer.parseInt(tagIdStr));
            }
        }

        logger.info("getOrderCommentList|parseListPara|转化后参数|"+req);

        if (hasNoParaFlag) {
            throw new InvalidParameterException("请求参数不能为空");
        }
        return req;
    }


    /*
    *使评价失效
    */
    @RequestMapping("/invalidate")
    @ResponseBody
    public CommonRes invalidate(@RequestBody ChangeCommentStatusReq changeCommentStatusReq){
        long startTime =  System.currentTimeMillis();
        logger.info("invalidate|评价失效|req:"+ changeCommentStatusReq);
        try{
            changeCommentStatusReq.setStatus(Boolean.FALSE);
            CommonRes res = null;
            try {
                evaluateHttpService.changeStatus(changeCommentStatusReq);
                res = new CommonRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),"success");
            } catch (FeedException e) {
                res = new  CommonRes(Boolean.FALSE, String.valueOf(e.getCode()),e.getMsg());
            }
            logger.info("invalidate|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("invalidate|程序异常|:"+e.getMessage());
            return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"评价失效异常");
        }
    }

    /*
    *使评价有效
    */
    @RequestMapping("/valid")
    @ResponseBody
    public CommonRes valid(@RequestBody ChangeCommentStatusReq changeCommentStatusReq){
        long startTime =  System.currentTimeMillis();
        logger.info("valid|评价有效|req:"+ changeCommentStatusReq);
        try{
            changeCommentStatusReq.setStatus(Boolean.TRUE);
            CommonRes res = null;
            try {
                evaluateHttpService.changeStatus(changeCommentStatusReq);
                res = new CommonRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),"success");
            } catch (FeedException e) {
                res = new  CommonRes(Boolean.FALSE, String.valueOf(e.getCode()),e.getMsg());
            }
            logger.info("valid|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("valid|程序异常|:"+e.getMessage());
            return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"评价有效异常");
        }
    }


    /*
   *  获取司机平均分
   */
    @RequestMapping("/getDriverAvgScore")
    @ResponseBody
    public GetDriverAvgScoreRes getDriverAvgScore(HttpServletRequest request){
        try{
            long startTime =  System.currentTimeMillis();
            String driverIdReq = request.getParameter("driverId");
            logger.info("getDriverAvgScore|获取司机平均分|driverId:"+driverIdReq);
            if(StringUtils.isEmpty(driverIdReq)){
                return new GetDriverAvgScoreRes(Boolean.FALSE,CodeTypes.ParaError.getCode(),"司机Id不能为空");
            }else if (!FeedStringUtil.isLong(driverIdReq)){
                return new GetDriverAvgScoreRes(Boolean.FALSE,CodeTypes.ParaError.getCode(),"司机Id必须为Long类型");
            }

            Long driverId = Long.parseLong(driverIdReq);
            int driverAvgScore = 0;
            try {
                driverAvgScore = evaluateHttpService.getDriverAvgScore(driverId);
            } catch (FeedException e) {
                return new GetDriverAvgScoreRes(Boolean.FALSE,CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
            }

            GetDriverAvgScoreRes res = new GetDriverAvgScoreRes(Boolean.TRUE,CodeTypes.Success.getCode(),"查询司机平均分成功",driverId,driverAvgScore);
            logger.info("getDriverAvgScore|" + (System.currentTimeMillis()-startTime)+"ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("getDriverAvgScore|程序异常|:"+e.getMessage());
            return  new GetDriverAvgScoreRes(Boolean.FALSE,CodeTypes.SystemError.getCode(),"程序异常");
        }
    }

    /*
    *解析String参数
    */
    private AddCommentReq parsePara(WebAddCommentReq webAddCommentReq) throws InvalidParameterException {
        AddCommentReq domainReq = new AddCommentReq();
        if(null == webAddCommentReq){
            throw new InvalidParameterException("参数不能为空");
        }

        String userId = webAddCommentReq.getUserId();
        if(StringUtils.isEmpty(userId)){
            throw new InvalidParameterException("用户ID不能为空");
        }else if(!FeedStringUtil.isInteger(userId)){
            throw new InvalidParameterException("用户ID格式必须为数字");
        }else{
            domainReq.setUserId(Integer.parseInt(userId));
        }


        String orderId = webAddCommentReq.getOrderId();
        if(StringUtils.isEmpty(orderId)){
            throw new InvalidParameterException("订单号不能为空");
        }else if(!FeedStringUtil.isLong(orderId)){
            throw new InvalidParameterException("订单号格式必须为数字");
        }else {
            domainReq.setOrderId(Long.parseLong(orderId));
        }

        domainReq.setContent(webAddCommentReq.getContent());

        String score = webAddCommentReq.getScore();
        if(StringUtils.isEmpty(score)){
            throw new InvalidParameterException("评价分数不能为空");
        }else if(!FeedStringUtil.isShort(score)){
            throw new InvalidParameterException("评价分数格式必须为数字");
        }else {
            domainReq.setScore(Short.parseShort(score));
        }

        List<String> tags = webAddCommentReq.getCommentTagIds();
        List<Integer> iTags = new ArrayList<Integer>();
        if(null != tags &&  tags.size() > 0){
            Iterator<String> it = tags.iterator();
            while (it.hasNext()){
                String tagStr = it.next();
                if(StringUtils.isEmpty(tagStr)){
                    logger.warn("parsePara|标签参数中存在空串");
                    continue;
                }
                if(FeedStringUtil.isInteger(tagStr)){
                    iTags.add(Integer.parseInt(tagStr));
                }else{
                    throw new InvalidParameterException("评价分标签ID格式必须为数字");
                }
            }
        }
        domainReq.setCommentTagIds(iTags);
        //此参数 非必填
        String beNeedCommentTagStr = webAddCommentReq.getBeNeedCommentTag();
        if(!StringUtils.isEmpty(beNeedCommentTagStr)){
            if(FeedStringUtil.isBoolean(beNeedCommentTagStr)){
                domainReq.setBeNeedCommentTag(Boolean.parseBoolean(beNeedCommentTagStr));
            }
        }

        //订单信息
        WebOrderInfo webOrderInfo = webAddCommentReq.getOrderInfo();
        if(null == webOrderInfo){
            throw new InvalidParameterException("订单信息不能为Null");
        }
        OrderInfo orderInfo = new OrderInfo();
        domainReq.setOrderInfo(orderInfo);
        orderInfo.setOrderId(domainReq.getOrderId());

        String driverIdStr = webOrderInfo.getDriverId();
        if(StringUtils.isEmpty(driverIdStr)){
            throw new InvalidParameterException("司机ID不能为空");
        }else if (!FeedStringUtil.isInteger(driverIdStr)){
            throw new InvalidParameterException("司机ID格式必须为数字");
        }else{
            orderInfo.setDriverId(Integer.parseInt(driverIdStr));
        }

        String city = webOrderInfo.getCity();
        if(StringUtils.isEmpty(city)){
            throw new InvalidParameterException("城市不能为空");
        }else{
            orderInfo.setCity(city);
        }

        String carTypeIdStr = webOrderInfo.getCarTypeId();
        if(StringUtils.isEmpty(carTypeIdStr)){
            throw new InvalidParameterException("车型ID不能为空");
        }else if (!FeedStringUtil.isInteger(carTypeIdStr)){
            throw new InvalidParameterException("司机ID格式必须为数字");
        }else{
            orderInfo.setCarTypeId(Integer.parseInt(carTypeIdStr));
        }

        String productTypeIdStr = webOrderInfo.getProductTypeId();
        if(StringUtils.isEmpty(productTypeIdStr)){
            throw new InvalidParameterException("产品类型ID不能为空");
        }else if (!FeedStringUtil.isInteger(productTypeIdStr)){
            throw new InvalidParameterException("产品类型ID格式必须为数字");
        }else{
            orderInfo.setProductTypeId(Integer.parseInt(productTypeIdStr));
        }

        //PHP时间戳是10位  java是13位；这里暂时处理
        String endTimeStr = webOrderInfo.getEndTime();
        if(StringUtils.isEmpty(endTimeStr)){
            throw new InvalidParameterException("订单结束时间不能为空");
        }else if (!FeedStringUtil.isInteger(endTimeStr)){
            throw new InvalidParameterException("订单结束时间D格式必须为时间戳");
        }else{
            orderInfo.setEndTime(Integer.parseInt(endTimeStr));
        }

        logger.info("parsePara|解析后参数："+domainReq);
        return domainReq;
    }


}
