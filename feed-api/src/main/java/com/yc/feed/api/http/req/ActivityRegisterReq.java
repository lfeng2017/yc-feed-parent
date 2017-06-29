package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/10/18.
 * 活动注册请求
 */
public class ActivityRegisterReq {
    //活动ID
    private long activityId;
    //活动开始时间
    private String startTime;
    //活动结束时间
    private String endTime;

    @Override
    public String toString() {
        return "ActivityRegisterReq{" +
                "activityId=" + activityId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
