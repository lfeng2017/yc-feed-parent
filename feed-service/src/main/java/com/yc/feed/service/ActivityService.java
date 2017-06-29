package com.yc.feed.service;

import com.yc.feed.api.http.req.ActivityRegisterReq;
import com.yc.feed.domain.excep.FeedException;

/**
 * Created by yusong on 2016/10/18.
 * 活动相关服务
 */
public interface ActivityService {

    /*
    *转发活动注册信息到MQ
    */
    public boolean transfer2Mq ( ActivityRegisterReq activityRegisterReq) throws FeedException;


}
