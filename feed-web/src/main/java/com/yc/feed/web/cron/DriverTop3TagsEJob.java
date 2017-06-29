package com.yc.feed.web.cron;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.yc.feed.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lfeng  on 2017/3/13.
 */
@Component
public class DriverTop3TagsEJob implements SimpleJob {
    @Autowired
    private TagService tagService;

    @Override
    public void execute(ShardingContext context) {
        int currentShard = context.getShardingItem();
        int shardTotal = context.getShardingTotalCount();
        tagService.runDriverTop3Tags(shardTotal,currentShard);
    }
}
