package com.yc.feed.domain.req;

/**
 * Created by yusong on 2016/11/14.
 * 添加收藏司机请求
 */
public class AddCollectDriverReq {
    //用户ID
    private Long userId;
    //司机ID
    private Long driverId;
    //司机备注
    private String driverNote;
    //司机所在城市
    private String driverCity;

    @Override
    public String toString() {
        return "AddCollectDriverReq{" +
                "userId=" + userId +
                ", driverId=" + driverId +
                ", driverNote='" + driverNote + '\'' +
                ", driverCity='" + driverCity + '\'' +
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

    public String getDriverNote() {
        return driverNote;
    }

    public void setDriverNote(String driverNote) {
        this.driverNote = driverNote;
    }

    public String getDriverCity() {
        return driverCity;
    }

    public void setDriverCity(String driverCity) {
        this.driverCity = driverCity;
    }
}
