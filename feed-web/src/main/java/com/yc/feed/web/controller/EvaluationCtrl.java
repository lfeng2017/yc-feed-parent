package com.yc.feed.web.controller;

import com.yc.feed.api.http.req.EvaluationRateReq;
import com.yc.feed.api.http.res.EvaluationRateRes;
import com.yc.feed.api.http.res.HisRateRes;
import com.yc.feed.api.http.res.Last50ordersRes;
import com.yc.feed.api.http.res.MonthsEvaluationRes;
import com.yc.feed.api.util.DateUtil;
import com.yc.feed.api.util.FeedStringUtil;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.service.EvaluateHttpService;
import com.yc.feed.service.HistoryEvalRateService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yusong on 2016/10/18.
 * 获取司机好评率控制器
 */
@Controller
@RequestMapping("/feed")
public class EvaluationCtrl {

    private final Logger logger = Logger.getLogger(EvaluationCtrl.class);

    @Autowired
    private EvaluateHttpService evaluateHttpService;

    @Autowired
    private HistoryEvalRateService historyEvalRateService;

    /*
   *获取司机参与各个活动的好评率
   */
    @RequestMapping("/getEvaluation")
    @ResponseBody
    public EvaluationRateRes getEvaluation(@RequestBody EvaluationRateReq evaluationRateReq){
        long startTime =  System.currentTimeMillis();
        try{
            logger.info("getEvaluation|getInfo:"+evaluationRateReq);
            EvaluationRateRes rateRes = evaluateHttpService.getDriverEvaluation(evaluationRateReq);
            logger.info("getEvaluation|"+(System.currentTimeMillis()-startTime)+ "ms|returnResult:"+rateRes);
            return rateRes;
        }catch (Exception e){
            logger.error("getEvaluation|程序异常|:"+e.getMessage());
        }
        return new EvaluationRateRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"程序异常");
    }


    /*
    *获取司机最近50单好评率
    */
    @RequestMapping("/getHisEvaluation")
    @ResponseBody
    public HisRateRes getHisEvaluation(HttpServletRequest request ){
        long startTime =  System.currentTimeMillis();
        try{
            String driverId = request.getParameter("driverId");
            logger.info("getHisEvaluation|driverId:"+driverId);
            if(!FeedStringUtil.isLong(driverId)) {
                return new HisRateRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),"driverId must be number");
            }
            HisRateRes res = historyEvalRateService.getHisEvaluation(Long.parseLong(driverId));
            logger.info("getHisEvaluation|"+(System.currentTimeMillis()-startTime)+ "ms|returnResult:"+res);
            return res;
        }catch (Exception e){
            logger.error("getHisEvaluation|程序异常|:"+e.getMessage());
        }
        return new HisRateRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"程序异常");
    }

    /*
    *查询最近50单订单号
    */
    @RequestMapping("/getLast50orders")
    @ResponseBody
    public Last50ordersRes getLast50orders(HttpServletRequest request ) {
        long startTime =  System.currentTimeMillis();
        try{
            String driverId = request.getParameter("driverId");
            logger.info("getLast50orders|driverId:" + driverId);
            if(!FeedStringUtil.isLong(driverId)) {
                return new Last50ordersRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),"driverId must be number");
            }
            String[] res  = null;
            long driverIdNum = Long.parseLong(driverId);
            try {
                res = historyEvalRateService.getLastOrder(driverIdNum);
            } catch (FeedException e) {
                logger.error("getLast50orders|查询最近50单错误|driverId:"+driverId,e);
                return new Last50ordersRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),driverIdNum,new String[]{});
            }
            Last50ordersRes result = new Last50ordersRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),driverIdNum,res);
            logger.info("getLast50orders|"+(System.currentTimeMillis()-startTime)+ "ms|returnResult:"+result);
            return  result;
        }catch (Exception e){
            logger.error("getLast50orders|程序异常|:"+e.getMessage());
        }
        return new Last50ordersRes(Boolean.FALSE,String.valueOf(CodeTypes.SystemError.getCode()),"程序异常");
    }

    /*
    *查询自然月的好评率和差评数
    */
    @RequestMapping("/getMonthsEvaluation")
    @ResponseBody
    public MonthsEvaluationRes getMonthsEvaluation(HttpServletRequest request ) {
         String yearMonth = request.getParameter("time");
	     String strdriverId = request.getParameter("driverId");
    	try {
	        logger.info("getMonthsEvaluation|time:" + yearMonth);
	        StringBuilder errorString = new StringBuilder();
	        String[] driverIds = strdriverId.split(",");
	        for (String id:driverIds) {
	        	  if(!FeedStringUtil.isLong(id)) {
	                  errorString.append("driverId must be number | ");
	                  continue;
	              }
	        }
	      
	        long startTime = 0;
	        long endTime = 0;
	        if(!FeedStringUtil.isInteger(yearMonth)) {
	            errorString.append("time must be number");
	        } else {
	    	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	          if(!StringUtils.isEmpty(yearMonth)){
	              try {
	            	  int monthDays = DateUtil.getMonthDays(Integer.valueOf(yearMonth.substring(0, 4)),
	            			  Integer.valueOf(yearMonth.substring(4)));
	            	  StringBuilder sb = new StringBuilder();
	            	  sb.append(yearMonth.substring(0, 4)).append("-").append(yearMonth.substring(4)).append("-");
	            	  sb.append("01").append(" ").append("00:00:00");
	            	  startTime = sdf.parse(sb.toString()).getTime()/1000;
	            	  
	            	  sb = new StringBuilder();
	            	  sb.append(yearMonth.substring(0, 4)).append("-").append(yearMonth.substring(4)).append("-");
	            	  sb.append(monthDays).append(" ").append("59:59:59");
	            	  endTime = sdf.parse(sb.toString()).getTime()/1000;
	              } catch (ParseException e) {
	            	  errorString.append("startTime格式为yyyy-MM-dd HH:mm:ss" + e);
	              }
	          }
	        }
	        if (!FeedStringUtil.isNullOrEmpty(errorString.toString())) {
	            return new MonthsEvaluationRes(Boolean.FALSE,CodeTypes.ParaError.getCodeStr(),errorString.toString());
	        }
            logger.info("getMonthsEvaluation|req:" + startTime + ":" + endTime + ":" +strdriverId);
	        MonthsEvaluationRes res  = evaluateHttpService.getMonthsEvaluation(startTime, endTime, strdriverId);
	        logger.info("getMonthsEvaluation|result:" + res);
	        return  res;
        
        } catch (Exception e) {
            logger.error("getMonthsEvaluation|获取近一个月好评信息错误|time:driverId"+yearMonth+"|"+strdriverId,e);
            return new MonthsEvaluationRes(Boolean.FALSE,CodeTypes.SystemError.getCodeStr(),CodeTypes.SystemError.getMsg());
        }
    }
}
