package com.yc.feed.web.controller;

import com.yc.feed.api.http.req.ActivityRegisterReq;
import com.yc.feed.api.http.res.ActivityRegisterRes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.service.ActivityService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yusong on 2016/10/18.
 * 活动注册控制器
 */
@Controller
@RequestMapping("/feed")
public class ActivityRegisterCtrl {

    private final Logger logger = Logger.getLogger(ActivityRegisterCtrl.class);

    @Autowired
    private ActivityService activityService;

    /*
    *活动注册接口
    *新增活动、修改活动都走此接口
    */
    @RequestMapping("/registerActivity")
    @ResponseBody
    public ActivityRegisterRes registerActivity(@RequestBody ActivityRegisterReq activityRegisterReq){
        logger.info("registerActivity|getInfo:"+activityRegisterReq);
        long startTime =  System.currentTimeMillis();
        try{
            ActivityRegisterRes res = null;
            boolean success = false;
            try {
                success =  activityService.transfer2Mq(activityRegisterReq);
            } catch (FeedException e) {
                logger.error("registerActivity|处理异常|",e);
                res = new ActivityRegisterRes(false,e.getCode(),e.getMsg());
                return res;
            }
            if(success){
                res = new ActivityRegisterRes(true,200,"接收成功");
            }else{
                res = new ActivityRegisterRes(false,300,"发送MQ失败");
            }
            logger.info("registerActivity|"+(System.currentTimeMillis()-startTime)+"ms|returnResult"+res);
            return res;
        }catch (Exception e){
            logger.error("registerActivity|程序异常|:"+e.getMessage());
        }
        return new ActivityRegisterRes(false,500,"发送MQ失败");
    }
}
