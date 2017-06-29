package com.yc.feed.web.controller;

import com.yc.feed.api.http.res.CommonRes;
import com.yc.feed.api.http.res.GetDriverTagStatRes;
import com.yc.feed.api.util.FeedStringUtil;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.service.StatService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yusong on 2016/10/15.
 * 统计信息控制器
 */
@Controller
@RequestMapping("/stat")
public class StatisticsCtrl {

    private final Logger logger = Logger.getLogger(StatisticsCtrl.class);

    @Autowired
    private StatService statService;

    /*
    *获取司机评价标签统计
    */
    @RequestMapping("/driver/getTagStat")
    @ResponseBody
    public GetDriverTagStatRes getDriverTagStat(HttpServletRequest request ){
        long startTime =  System.currentTimeMillis();
        try{
            String driverIdStr = request.getParameter("driverId");
            logger.info("getDriverTagStat|driverId:"+driverIdStr);
            if(StringUtils.isEmpty(driverIdStr)){
                return new GetDriverTagStatRes(Boolean.FALSE, CodeTypes.ParaError.getCodeStr(),"driverId can't be null");
            }else if (!FeedStringUtil.isLong(driverIdStr)){
                return new GetDriverTagStatRes(Boolean.FALSE, CodeTypes.ParaError.getCodeStr(),"driverId must be a number");
            }
            Long driverId = Long.parseLong(driverIdStr);
            GetDriverTagStatRes res = statService.getDriverTagStat(driverId);
            logger.info("getDriverTagStat|获取司机评价标签统计成功:"+(System.currentTimeMillis()-startTime)+"ms|"+res);
            return res;
        }catch (Exception e){
            logger.error("getDriverTagStat|程序异常|:"+e.getMessage());
            return new GetDriverTagStatRes(Boolean.FALSE, CodeTypes.SystemError.getCodeStr(),"程序异常");
        }
    }
}
