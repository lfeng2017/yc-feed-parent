package com.yc.feed.dao.shard;

import com.yc.feed.api.http.req.GetCollectInfoReq;
import com.yc.feed.domain.entity.CollectDriver;
import com.yc.feed.domain.entity.CollectDriverInfo;
import com.yc.feed.domain.entity.FeedCollectDriverInfo;
import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.annotation.Sharding;
import org.jfaster.mango.annotation.ShardingBy;
import org.jfaster.mango.sharding.DatabaseShardingStrategy;
import org.jfaster.mango.sharding.ShardingStrategy;

import java.util.List;

/*目前该类没有用*/
@DB(database="", table = "collect_driver")
public interface FeedCollectDriverMangoDao {

    String COLUMNS = "user_id, driver_id, create_time, update_time, service_times, status, driver_note, driver_city";

    String COLUMNS_ALL = "collect_driver_id," + COLUMNS;
    /*@SQL("select user_id from #table where driver_id=:1 and status=1")
    List<Long> getCollectListByDriverId(long driverId);

    @SQL("select count() from #table where driver_id=:1 and status=1")
    int getUserCountByDriverId(long driverId);

    @SQL("select user_id from #table where driver_id=:1 and status=1 order by create_time DESC limit :2 offset :3")
    List<Long> getCollectListByPage(long driverId, int pageSize, long offSetSize);

    @SQL("select count() from #table where driver_id=:1 and user_id=:2 and status=1")
    int getServiceTimesById(long driverId, long userId);

    @SQL("select user_id from #table where driver_id=:1 and status=1 and create_time >:2 and create_time < :3")
    List<Long> getCollectListByTime(long driverId, long startTime, long endTime);

    @SQL("update #table set driver_note=:3 where driver_id=:1 and user_id=:2 and status=1")
    void editDriverNote(long driverId, long userId, String notes);

    @SQL("select driver_note from  #table  where driver_id=:1 and user_id=:2 and status=1")
    String getDriverNote(long driverId, long userId);

    @SQL("select driver_id from #table where user_id=:1 and status=1")
    List<Long> getCollectListByUserId(long userId);

    @SQL("update #table set status=2 where driver_id=:1 and user_id=:1 and status=1")
    void cancelCollect(long driverId, long userId);*/

    @SQL("insert into  #table(" + COLUMNS + ") values(:userId, :driverId, :createTime, :updateTime, :serviceTimes, :status, :driverNote, :driverCity)")
    @Sharding(shardingStrategy = FeedCollectDriverMangoDao.FeedDatabaseShardingByDriverId.class)
    void add(@ShardingBy("driverId")CollectDriver info);

    @SQL("update #table(" + COLUMNS + ") set #if(:1.userId!=null)userId #end =#if(:1.userId!=null):1.userId," +
            "#if(:1.driverId!=null)driverId #end =#if(:1.driverId!=null):1.driverId," +
            "#if(:1.createTime!=null)createTime #end =#if(:1.createTime!=null):1.createTime," +
            "#if(:1.updateTime!=null)updateTime #end =#if(:1.updateTime!=null):1.updateTime," +
            "#if(:1.serviceTimes!=null)serviceTimes #end =#if(:1.serviceTimes!=null):1.serviceTimes," +
            "#if(:1.status!=null)status #end = #if(:1.status!=null):1.status," +
            "#if(:1.driverNote!=null)driverNote #end =#if(:1.driverNote!=null):1.driverNote," +
            "#if(:1.driverCity!=null)driverCity #end =#if(:1.driverCity!=null):1.driverCity" +
            "where collect_driver_id = 1.collectDriverId")
    @Sharding(shardingStrategy = FeedCollectDriverMangoDao.FeedDatabaseShardingByUserId.class)
    void updateByPrimaryKey(@ShardingBy("driverId")CollectDriver info);

    @SQL("update #table(" + COLUMNS + ") set #if(:1.userId!=null)userId #end =#if(:1.userId!=null):1.userId," +
            "#if(:1.driverId!=null)driverId #end =#if(:1.driverId!=null):1.driverId," +
            "#if(:1.createTime!=null)createTime #end =#if(:1.createTime!=null):1.createTime," +
            "#if(:1.updateTime!=null)updateTime #end =#if(:1.updateTime!=null):1.updateTime," +
            "#if(:1.serviceTimes!=null)serviceTimes #end =#if(:1.serviceTimes!=null):1.serviceTimes," +
            "#if(:1.status!=null)status #end = #if(:1.status!=null):1.status," +
            "#if(:1.driverNote!=null)driverNote #end =#if(:1.driverNote!=null):1.driverNote," +
            "#if(:1.driverCity!=null)driverCity #end =#if(:1.driverCity!=null):1.driverCity" +
            "where driverId = 1.driverId and userId=1.userId")
    void updateByUserAndDriverId(@ShardingBy("driverId")CollectDriver info);

    @SQL("select count(*) from #table " +
            "where status=1 #if(:1.driverId!=null) and driverId #end =#if(:1.driverId!=null):1.driverId" +
             "#if(:1.userId!=null) and userId #end =#if(:1.userId!=null):1.userId")
    int getCollectDriversCount(@ShardingBy("driverId") GetCollectInfoReq getCollectInfoReq);

    @SQL("select " + COLUMNS_ALL + " from #table where status=1 " +
            "#if(:1.driverId!=null) and driverId #end =#if(:1.driverId!=null):1.driverId" +
            "#if(:1.userId!=null) and userId #end =#if(:1.userId!=null):1.userId")
    List<CollectDriver> list(@ShardingBy("driverId") GetCollectInfoReq getCollectInfoReq);

    @SQL("select " + COLUMNS_ALL + " from #table where 1=1" +
            "#if(:1.driverId!=null) and driverId #end =#if(:1.driverId!=null):1.driverId" +
            "#if(:1.userId!=null) and userId #end =#if(:1.userId!=null):1.userId")
    List<CollectDriver> selectAllList(@ShardingBy("driverId") GetCollectInfoReq getCollectInfoReq);


    class FeedDatabaseShardingByDriverId implements ShardingStrategy<Long, Long> {

        @Override
        public String getDatabase(Long driverId) {
            return "yc_feed_" + (driverId % 128);
        }
        @Override
        public String getTargetTable(String table, Long driverId) {
            return "collect_driver";
        }
    }

    class FeedDatabaseShardingByUserId implements ShardingStrategy<Long, Long> {

        @Override
        public String getDatabase(Long userId) {
            return "yc_feed_" + (userId % 128);
        }
        @Override
        public String getTargetTable(String table, Long driverId) {
            return "collect_driver";
        }
    }
}

