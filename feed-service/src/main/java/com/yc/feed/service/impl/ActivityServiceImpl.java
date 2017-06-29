package com.yc.feed.service.impl;

import com.yc.feed.api.http.req.ActivityRegisterReq;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.service.ActivityService;
import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.InvalidParameterException;

/**
 * Created by yusong on 2016/10/18.
 * 活动相关服务实现
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    private final Logger logger = Logger.getLogger(ActivityServiceImpl.class);

    @Resource
    private AmqpTemplate amqpTemplateActivity;

    @Override
    public boolean transfer2Mq(ActivityRegisterReq activityRegisterReq) throws FeedException{
        logger.info("transfer2Mq|发送活动注册通知|START|"+activityRegisterReq);
        try {
            validPara(activityRegisterReq);
        } catch (InvalidParameterException e) {
            logger.error("transfer2Mq|参数错误|", e);
            throw new FeedException(CodeTypes.ParaError.getCode(),e.getMessage());
        }
        try {
            amqpTemplateActivity.convertAndSend(activityRegisterReq);
            logger.info("transfer2Mq|发送活动注册通知|END|"+activityRegisterReq);
        } catch (AmqpException e) {
            logger.error("transfer2Mq|发送活动注册通知错误|"+activityRegisterReq,e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /*
    *参数检查
    */
    private void validPara(ActivityRegisterReq activityRegisterReq) throws InvalidParameterException{
        if(null == activityRegisterReq){
            throw new InvalidParameterException("参数不能为Null");
        }
        if(StringUtils.isEmpty(activityRegisterReq.getStartTime())){
            throw new InvalidParameterException("活动开始时间不能为Null");
        }
        if(StringUtils.isEmpty(activityRegisterReq.getEndTime())){
            throw new InvalidParameterException("活动结束时间不能为Null");
        }
    }

}
