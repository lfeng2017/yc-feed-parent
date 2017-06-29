package com.yc.feed.web.cron;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.yc.feed.service.DriverRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lfeng on 2017/2/17.
 *
 * 使用elastic-job来重构driver-API
 * 隔天定时跑司机的好评率
 */

@Component
public class DriverEvaluationEJob implements SimpleJob {
    @Autowired
    private DriverRateService driverRateService;

    @Override
    public void execute(ShardingContext context) {
        int currentShard = context.getShardingItem();
        int shardTotal = context.getShardingTotalCount();
        driverRateService.runDriverCommentEvaluation(shardTotal,currentShard);
    }
}
