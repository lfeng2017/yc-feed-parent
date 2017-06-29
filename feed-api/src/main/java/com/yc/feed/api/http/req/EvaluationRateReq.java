package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/10/17.
 * 查询司机好评率
 */
public class EvaluationRateReq {
    //活动ID
    private long activityId;
    //司机ID
    private long driverId;

    @Override
    public String toString() {
        return "EvaluationRateReq{" +
                "activityId=" + activityId +
                ", driverId=" + driverId +
                '}';
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }
}
