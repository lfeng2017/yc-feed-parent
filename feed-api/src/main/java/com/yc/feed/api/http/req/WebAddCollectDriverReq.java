package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/11/14.
 * 添加收藏司机请求
 */
public class WebAddCollectDriverReq {
    //用户ID
    private String userId;
    //司机ID
    private String driverId;
    //司机备注
    private String driverNote;
    //司机所在城市
    private String driverCity;

    @Override
    public String toString() {
        return "WebAddCollectDriverReq{" +
                "userId='" + userId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", driverNote='" + driverNote + '\'' +
                ", driverCity='" + driverCity + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
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
