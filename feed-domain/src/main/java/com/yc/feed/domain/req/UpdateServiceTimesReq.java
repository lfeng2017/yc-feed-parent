package com.yc.feed.domain.req;

/**
 * Created by yusong on 2016/12/15.
 * 更新服务次数
 */
public class UpdateServiceTimesReq {
    //用户Id
    private Long userId;
    //司机ID
    private Long driverId;
    //服务次数
    private Integer serviceTimes;

    @Override
    public String toString() {
        return "UpdateServiceTimesReq{" +
                "userId=" + userId +
                ", driverId=" + driverId +
                ", serviceTimes=" + serviceTimes +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Integer getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(Integer serviceTimes) {
        this.serviceTimes = serviceTimes;
    }
}
