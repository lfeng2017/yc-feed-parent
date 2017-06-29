package com.yc.feed.service;

import com.yc.feed.api.http.res.GetDriverTagStatRes;
import com.yc.feed.domain.excep.FeedException;

/**
 * Created by yusong on 2016/11/15.
 * 统计服务
 */
public interface StatService {


    /*
    *获取司机标签统计
    */
    public GetDriverTagStatRes getDriverTagStat(Long driverId);






}
