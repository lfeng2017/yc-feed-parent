package com.yc.feed.service;

import com.yc.feed.domain.model.DriverCommentEvaluationInfo;

/**
 * Created by lfeng on 2016/11/23.
 * 司机最近50单好评率结果处理
 */
public interface DriverRateService {

    /*
    * 司机最近50单好评率结果处理
    */
    public boolean dealDriverRate(DriverCommentEvaluationInfo driverRateResult);

    public void runDriverCommentEvaluation(int shardTotal,int currentShard);
}
