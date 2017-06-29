package com.yc.feed.web.controller;

import com.yc.feed.api.http.req.WebAddJudgementReq;
import com.yc.feed.api.http.req.WebAddJudgementTagReq;
import com.yc.feed.api.http.res.CommonRes;
import com.yc.feed.api.http.res.GetAllTagRes;
import com.yc.feed.api.http.res.GetJudgementListRes;
import com.yc.feed.api.http.res.GetJudgementTagRes;
import com.yc.feed.api.util.FeedStringUtil;
import com.yc.feed.api.util.ParaUtil;
import com.yc.feed.domain.entity.JudgeOfUser;
import com.yc.feed.domain.entity.OrderCommentUserTag;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.req.AddJudgementReq;
import com.yc.feed.domain.req.GetJudgementReq;
import com.yc.feed.service.JudgementService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by yusong on 2016/10/18.
 * 获取司机好评率控制器
 */
@Controller
@RequestMapping("/feed/driver")
public class DriverJudgeCtrl {

    private final Logger logger = Logger.getLogger(DriverJudgeCtrl.class);

    @Resource
    private JudgementService judgementService;

    /*
    *添加评价
    */
    @RequestMapping("/addJudgement")
    @ResponseBody
    public CommonRes addJudgement(@RequestBody WebAddJudgementReq addJudgementReq){
        long startTime =  System.currentTimeMillis();
        try {
            AddJudgementReq domainReq = parseAddPara(addJudgementReq);
            try {
                judgementService.insertJudgement(domainReq);
            } catch (FeedException e) {
                logger.error("addJudgement|新增司机评价用户错误|");
                return new CommonRes(Boolean.FALSE,String.valueOf(e.getCode()),e.getMessage());
            }
            logger.info("addJudgement|新增司机评价用户成功:"+(System.currentTimeMillis()-startTime)+"ms");
            return new CommonRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),"add success");
        }catch (Exception e){
            logger.error("addJudgement|程序异常|:"+e.getMessage());
        }
        return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"新增司机评价用户异常");
    }

    /*
    *获取司机评价乘客列表
    */
    @RequestMapping("/getJudgementList")
    @ResponseBody
    public GetJudgementListRes getJudgementList(HttpServletRequest request){
        long startTime =  System.currentTimeMillis();
        try{
            GetJudgementReq domainReq = null;
            try {
                domainReq = parseListPara(request);
            } catch (InvalidParameterException e) {
                logger.error("getJudgementList|参数错误|",e);
                return new GetJudgementListRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),e.getMessage());
            }
            logger.info("getJudgementList|req:"+request);
            GetJudgementListRes result = judgementService.getList(domainReq);
            logger.info("addJudgement|获取司机评价乘客列表:"+(System.currentTimeMillis()-startTime)+"ms|res:"+result);
            return result;
        }catch (Exception e){
            logger.error("getJudgementList|程序异常|:"+e.getMessage());
        }
        return new GetJudgementListRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"获取司机评价乘客列表异常");
    }


    /*
    *司机评价乘客标签
    */
    @RequestMapping("/addJudgementTag")
    @ResponseBody
    public CommonRes addJudgementTag(@RequestBody WebAddJudgementTagReq addJudgementReq){
        long startTime =  System.currentTimeMillis();
        try{
            logger.info("addJudgementTag|新增司机评价用户标签|req:"+addJudgementReq);
            try {
                judgementService.insertJudgementTag(addJudgementReq);
            } catch (FeedException e) {
                logger.error("addJudgementTag|新增司机评价用户标签错误|",e);
                return new CommonRes(Boolean.FALSE,String.valueOf(e.getCode()),e.getMessage());
            }
            logger.info("addJudgementTag|新增司机评价用户标签成功:"+(System.currentTimeMillis()-startTime)+"ms|serviceOrderId:"+addJudgementReq.getServiceOrderId());
            return new CommonRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),"add success");
        }catch (Exception e){
            logger.error("addJudgementTag|程序异常|:"+e.getMessage());
        }
        return new CommonRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"新增司机评价用户标签异常");
    }

    /*
    *获取司机评价乘客列表
    */
    @RequestMapping("/getJudgementTag")
    @ResponseBody
    public GetJudgementTagRes getJudgementTag(HttpServletRequest request){
        long startTime =  System.currentTimeMillis();
        try{
            String serviceOrderId = request.getParameter("serviceOrderId");
            if(StringUtils.isEmpty(serviceOrderId)){
                return new GetJudgementTagRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),"serviceOrderId is needed");
            }else if(!FeedStringUtil.isLong(serviceOrderId)){
                return new GetJudgementTagRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),"serviceOrderId must be number");
            }
            logger.info("getJudgementTag|查询司机评价用户标签|serviceOrderId:"+serviceOrderId);
            List<OrderCommentUserTag> tags = judgementService.getTagList(Long.parseLong(serviceOrderId));
            GetJudgementTagRes res = new GetJudgementTagRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),"success",(tags == null? 0:tags.size()),tags);
            logger.info("getJudgementTag|查询司机评价用户标签:"+(System.currentTimeMillis()-startTime)+"ms|res:"+res);
            return res;
        }catch (Exception e){
            logger.error("getJudgementTag|程序异常|:"+e.getMessage());
        }
        return new GetJudgementTagRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"查询司机评价用户标签异常");
    }




    /*
    *解析列表参数
    */
    private GetJudgementReq parseListPara(HttpServletRequest request)throws InvalidParameterException{
        GetJudgementReq getJudgementReq = new GetJudgementReq();
        String driverId = request.getParameter("driverId");
        String userId = request.getParameter("userId");
        String serviceOrderId = request.getParameter("serviceOrderId");

        ParaUtil.setPagePara(request,getJudgementReq);

        if(!StringUtils.isEmpty(userId)){
            if(!FeedStringUtil.isLong(userId)){
                throw new InvalidParameterException("用户ID格式必须为数字。");
            }else{
                getJudgementReq.setUserId(Long.parseLong(userId));
            }
        }

        if(!StringUtils.isEmpty(driverId)){
            if(!FeedStringUtil.isLong(driverId)){
                throw new InvalidParameterException("司机ID格式必须为数字。");
            }else{
                getJudgementReq.setDriverId(Long.parseLong(driverId));
            }
        }

        if(!StringUtils.isEmpty(serviceOrderId)){
            if(!FeedStringUtil.isLong(serviceOrderId)){
                throw new InvalidParameterException("serviceOrderId格式必须为数字。");
            }else{
                getJudgementReq.setServiceOrderId(Long.parseLong(serviceOrderId));
            }
        }
        logger.info("parseListPara|解析后参数:"+getJudgementReq);
        return getJudgementReq;
    }

    /*
    *解析添加参数
    */
    private AddJudgementReq parseAddPara(WebAddJudgementReq request) throws InvalidParameterException{
        logger.info("parseAddPara|HTTP参数："+request);
        if(null == request){
            logger.error("parseListPara|参数为空");
            throw new InvalidParameterException("参数不能为空");
        }
        String orderId = request.getOrderId();
        String userId = request.getUserId();
        String driverId = request.getDriverId();
        String score = request.getScore();
        String detail = request.getDetail();

        AddJudgementReq domainReq = new AddJudgementReq();

        if(StringUtils.isEmpty(orderId)){
            throw new InvalidParameterException("订单号不能为空");
        }else if(!FeedStringUtil.isLong(orderId)){
            throw new InvalidParameterException("订单号格式必须为数字");
        }else{
            domainReq.setOrderId(Long.parseLong(orderId));
        }

        if(StringUtils.isEmpty(userId)){
            throw new InvalidParameterException("用户ID不能为空");
        }else if(!FeedStringUtil.isLong(userId)){
            throw new InvalidParameterException("用户ID格式必须为数字");
        }else{
            domainReq.setUserId(Long.parseLong(userId));
        }

        if(StringUtils.isEmpty(driverId)){
            throw new InvalidParameterException("司机ID不能为空");
        }else if(!FeedStringUtil.isLong(driverId)){
            throw new InvalidParameterException("司机ID格式必须为数字");
        }else{
            domainReq.setDriverId(Long.parseLong(driverId));
        }

        if(StringUtils.isEmpty(score)){
            throw new InvalidParameterException("评价分数不能为空");
        }else if(!FeedStringUtil.isInteger(score)){
            throw new InvalidParameterException("评价分数格式必须为数字");
        }else{
            domainReq.setScore(Integer.parseInt(score));
        }
        domainReq.setDetail(detail);
        logger.info("parseAddPara|转化后参数|"+domainReq);
        return domainReq;
    }

}
