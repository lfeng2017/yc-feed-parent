package com.yc.feed.service;

import com.yc.feed.api.http.res.HisRateRes;
import com.yc.feed.domain.enums.ValidateTypes;
import com.yc.feed.domain.excep.FeedException;

/**
 * Created by yusong on 2016/11/23.
 * 历史好评服务
 */
public interface HistoryEvalRateService {


    /*
    *更新司机历史好评率
    * autoComment:是否是自动评价
    */
    public void updateDriverRate(Long driverId, Long orderId, ValidateTypes val,boolean autoComment);

    /*
    *获取司机历史好评率
    */
    public HisRateRes getHisEvaluation(Long driverId);

    /*
    *查询最近50单
    */
    public String[] getLastOrder(Long driverId) throws FeedException;

    /*
    *订单状态变更，更新好评率
    */
    public void updateRateByStatus(Long driverId, Long orderId,boolean status);



}
