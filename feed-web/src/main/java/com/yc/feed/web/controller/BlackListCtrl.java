package com.yc.feed.web.controller;

import com.yc.feed.api.http.req.WebAddBlackListReq;
import com.yc.feed.api.http.req.WebDelBlackListByInfoReq;
import com.yc.feed.api.http.req.WebDelBlackListReq;
import com.yc.feed.api.http.req.GetBlackListReq;
import com.yc.feed.api.http.res.AddCommentRes;
import com.yc.feed.api.http.res.CommonRes;
import com.yc.feed.api.http.res.GetBlackListByPageRes;
import com.yc.feed.api.http.res.GetJudgementListRes;
import com.yc.feed.api.util.FeedStringUtil;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.req.AddBlackListReq;
import com.yc.feed.service.BlackListService;
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

/**
 * Created by yusong on 2016/10/26.
 * 黑名单控制器
 */
@Controller
@RequestMapping("/feed/blackList")
public class BlackListCtrl {

    private final Logger logger = Logger.getLogger(BlackListCtrl.class);

    @Autowired
    private BlackListService blackListService;

    /*
    *全量获取黑名单
    */
    @RequestMapping("/getBlackList")
    @ResponseBody
    public GetBlackListByPageRes getBlackList(HttpServletRequest request ){
        long startTime =  System.currentTimeMillis();
        try{
            GetBlackListReq getBlackListReq=null;
            try{
                getBlackListReq = parseListPara(request);
                logger.info("getBlackList|获取黑名单参数:"+getBlackListReq);
            }catch (InvalidParameterException e) {
                logger.error("getBlackList|传递参数错误");
                return new GetBlackListByPageRes(Boolean.FALSE, CodeTypes.ParaError.getCode(), e.getMessage());
            }

            GetBlackListByPageRes res = blackListService.getBlackList(getBlackListReq);
            logger.info("getBlackList|获取黑名单响应:"+(System.currentTimeMillis()-startTime)+"ms|returnResult"+res);
            return res;
        }catch (Exception e){
            logger.error("getBlackList|程序异常|:"+e.getMessage());
        }
        return new GetBlackListByPageRes(Boolean.FALSE, String.valueOf(CodeTypes.SystemError.getCode()),"黑名单查询异常",0,null);
    }

    /*
    *新增黑名单
    */
    @RequestMapping("/addBlackList")
    @ResponseBody
    public CommonRes addBlackList(@RequestBody WebAddBlackListReq webAddBlackListReq){
        long startTime =  System.currentTimeMillis();
        try{
            AddBlackListReq domainReq=null;
            try {
                domainReq = parseAddPara(webAddBlackListReq);
            } catch (InvalidParameterException e) {
                logger.error("getJudgementList|参数错误|",e);
                return new GetJudgementListRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),e.getMessage());
            }
            try {
                blackListService.insertBlackList(domainReq);
            } catch (FeedException e) {
                logger.error("addBlackList|新增黑名单错误|");
                return new CommonRes(Boolean.FALSE,String.valueOf(e.getCode()),e.getMsg());
            }
            logger.info("addBlackList|新增黑名单成功:"+(System.currentTimeMillis()-startTime)+"ms|userId:"+webAddBlackListReq.getUserId());
            return new CommonRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),"新增黑名单成功");
        }catch (Exception e){
            logger.error("addBlackList|程序异常|"+webAddBlackListReq,e);
        }
        return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"新增黑名单异常");
    }

    /*
    *删除黑名单
    */
    @RequestMapping("/removeBlackList")
    @ResponseBody
    public CommonRes removeBlackList(@RequestBody WebDelBlackListReq delBlackListReq) {
        long startTime =  System.currentTimeMillis();
        try{
            logger.info("removeBlackList|删除黑名单:"+delBlackListReq);
            if (null == delBlackListReq || StringUtils.isEmpty(delBlackListReq.getBlackListId())){
                return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.ParaError.getCode()),"ID参数不能为空");
            }else if(!FeedStringUtil.isLong(delBlackListReq.getBlackListId())){
                return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.ParaError.getCode()),"ID参数必须为数字");
            }
            try {
                boolean deleteFlag = blackListService.deleteBlackList(Long.parseLong(delBlackListReq.getBlackListId()));
                if(deleteFlag){
                    logger.info("removeBlackList|删除黑名单成功:"+(System.currentTimeMillis()-startTime)+"ms|Id:"+delBlackListReq);
                    return new CommonRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),"删除黑名单成功");
                }
            } catch (FeedException e) {
                logger.error("removeBlackList|删除黑名单错误|");
                return new CommonRes(Boolean.FALSE,String.valueOf(e.getCode()),e.getMessage());
            }
        }catch (Exception e){
            logger.error("removeBlackList|程序异常|:"+e.getMessage());
        }
        return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"删除黑名单异常");
    }


    /*
*删除黑名单
*/
    @RequestMapping(value = "/removeByInfo", method = RequestMethod.POST)
    @ResponseBody
    public CommonRes removeByInfo(@RequestBody WebDelBlackListByInfoReq request) {
        long startTime =  System.currentTimeMillis();
        String driverId = request.getDriverId();
        String userId = request.getUserId();
        try{
            logger.info("removeByInfo|删除黑名单:" + driverId + ":" + userId);
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isEmpty(driverId) || !FeedStringUtil.isLong(driverId)){
                sb.append("driverId参数错误 |");
            }
            if(StringUtils.isEmpty(userId) || !FeedStringUtil.isLong(userId)){
                sb.append("UserId参数错误");
            }

            if (sb != null && sb.length() > 0) {
                return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.ParaError.getCode()),sb.toString());
            }
            try {
                boolean flag = blackListService.deleteBlackListByInfo(Long.valueOf(driverId), Long.valueOf(userId));
                if(flag){
                    logger.info("removeByInfo|删除黑名单成功:"+(System.currentTimeMillis()-startTime)+"ms|driverid:"+driverId+"userid:"+userId);
                    return new CommonRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),"删除黑名单成功");
                }
            } catch (FeedException e) {
                logger.error("removeByInfo|删除黑名单错误|");
                return new CommonRes(Boolean.FALSE,String.valueOf(e.getCode()),e.getMessage());
            }
            return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"删除黑名单异常");
        }catch (Exception e){
            logger.error("removeByInfo|程序异常|:"+e.getMessage());
            return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"删除黑名单异常");
        }
    }

    /*
    *解析获取黑名单列表HTTP参数
    */
    private GetBlackListReq parseListPara(HttpServletRequest request) throws InvalidParameterException {
        if(null == request){
            logger.error("parseListPara|参数为空");
            throw new InvalidParameterException("参数不能为空");
        }
        String userId = request.getParameter("userId");
        String driverId = request.getParameter("driverId");
        String type = request.getParameter("type");
        String beCount = request.getParameter("beCount");
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String blackListId = request.getParameter("blackListId");


        logger.info("getBlackList|parseListPara|解析前参数,userId:"+userId+"|driverId:"+driverId+"|type:"+type+"|beCount:"+beCount+"|blackListId:"+blackListId);
        GetBlackListReq req = new GetBlackListReq();
        if(!StringUtils.isEmpty(blackListId)){
            if(!FeedStringUtil.isLong(blackListId)){
                throw new InvalidParameterException("blackListId格式必须为数字。");
            }else{
                req.setBlackListId(Long.parseLong(blackListId));
            }
        }

        if(!StringUtils.isEmpty(userId)){
            if(!FeedStringUtil.isLong(userId)){
                throw new InvalidParameterException("用户ID格式必须为数字。");
            }else{
                req.setUserId(Long.parseLong(userId));
            }
        }


        if(!StringUtils.isEmpty(driverId)){
            if(!FeedStringUtil.isLong(driverId)){
                throw new InvalidParameterException("司机ID格式必须为数字。");
            }else{
                req.setDriverId(Long.parseLong(driverId));
            }
        }

        if(!StringUtils.isEmpty(type)){
            if(!FeedStringUtil.isInteger(type)){
                throw new InvalidParameterException("typeD格式必须为数字。");
            }else{
                req.setType(Integer.parseInt(type));
            }
        }

        if(!StringUtils.isEmpty(beCount)){
            if(!FeedStringUtil.isBoolean(beCount)){
                throw new InvalidParameterException("beCount格式必须为布尔型。");
            }else{
                req.setBeCount(Boolean.parseBoolean(beCount));
            }
        }

        //分页参数
        if(!StringUtils.isEmpty(pageNum)){
            if(!FeedStringUtil.isLong(pageNum)){
                throw new InvalidParameterException("pageNum格式必须为数字。");
            }else{
                req.setPageNum(Long.parseLong(pageNum));
            }
        }

        if(!StringUtils.isEmpty(pageSize)){
            if(!FeedStringUtil.isLong(pageSize)){
                throw new InvalidParameterException("pageSize格式必须为数字。");
            }else{
                req.setPageSize(Long.parseLong(pageSize));
            }
        }


        logger.info("getBlackList|parseListPara|解析后参数:"+req);
        return req;
    }


    /*
   *解析新增参数
   */
    private AddBlackListReq parseAddPara(WebAddBlackListReq webAddBlackListReq)throws InvalidParameterException{
        logger.info("parseAddPara|新增黑名单请求|req:"+webAddBlackListReq);
        if (null == webAddBlackListReq){
            logger.error("parseAddPara|新增黑名单参数为空|");
            throw new InvalidParameterException("参数不能为空");
        }
        AddBlackListReq domainReq = new AddBlackListReq();
        String userId = webAddBlackListReq.getUserId();
        if(StringUtils.isEmpty(userId)){
            throw new InvalidParameterException("用户ID不能为空");
        }else if (!FeedStringUtil.isLong(userId)){
            throw new InvalidParameterException("用户ID格式必须为数字");
        }else{
            domainReq.setUserId(Long.parseLong(userId));
        }

        String driverId = webAddBlackListReq.getDriverId();
        if(StringUtils.isEmpty(driverId)){
            throw new InvalidParameterException("司机ID不能为空");
        }else if (!FeedStringUtil.isLong(driverId)){
            throw new InvalidParameterException("司机ID格式必须为数字");
        }else{
            domainReq.setDriverId(Long.parseLong(driverId));
        }

        String type = webAddBlackListReq.getType();
        if(StringUtils.isEmpty(type)){
            throw new InvalidParameterException("黑名单类型不能为空");
        }else if (!FeedStringUtil.isInteger(type)){
            throw new InvalidParameterException("黑名单类型格式必须为数字");
        }else{
            domainReq.setType(Integer.parseInt(type));
        }

        String serviceOrderId = webAddBlackListReq.getServiceOrderId();
        if(StringUtils.isEmpty(serviceOrderId)){
            throw new InvalidParameterException("订单号不能为空");
        }else if (!FeedStringUtil.isLong(serviceOrderId)){
            throw new InvalidParameterException("订单号格式必须为数字");
        }else{
            domainReq.setServiceOrderId(Long.parseLong(serviceOrderId));
        }

        logger.info("parseAddPara|新增黑名单转化后参数|"+domainReq);
        return domainReq;
    }

}


/*
刘海洋初步的需求：
获取司机的所有评价信息
评价订单
收藏司机
获取收藏司机列表
移除收藏司机
获取评价标签
​
添加黑名单
移除黑名单
获取黑名单司机列表
*/
