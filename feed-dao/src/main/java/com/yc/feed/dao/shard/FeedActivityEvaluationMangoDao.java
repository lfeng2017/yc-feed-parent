package com.yc.feed.dao.shard;

import com.yc.feed.domain.entity.FeedActivityEvaluationInfo;
import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.annotation.Sharding;
import org.jfaster.mango.annotation.ShardingBy;
import org.jfaster.mango.sharding.ShardingStrategy;

@DB(table = "feed_activity_evaluation")
@Sharding(shardingStrategy = FeedActivityEvaluationMangoDao.EvaluationShardingStrategy.class)
public interface FeedActivityEvaluationMangoDao {

    String COLUMNS = "activity_id, driver_id,last_order_id,orders, good_orders, bad_orders, create_time, evaluation, deleted";

    @SQL("select "+COLUMNS +" from #table where activity_id=:activityId and driver_id=:driverId")
    FeedActivityEvaluationInfo get(@ShardingBy("driverId") FeedActivityEvaluationInfo order);


    class EvaluationShardingStrategy implements ShardingStrategy<Long, Long> {

        @Override
        public String getDatabase(Long driverId) {
           return driverId % 128 < 64 ? "db1" : "db2";
        }

        @Override
        public String getTargetTable(String table, Long driverId) {
        	
            return "yc_feed_" + (driverId % 128) + "." + table;
        }

    }

}
