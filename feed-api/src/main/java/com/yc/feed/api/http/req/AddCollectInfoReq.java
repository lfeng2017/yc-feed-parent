package com.yc.feed.api.http.req;

/**
 * Created by ke on 2016/11/2.
 * 添加收藏司机
 */
public class AddCollectInfoReq {

    public String userId;
    public String driverId;
    public String driverNote;
    public String city;
    public String driverCity;

    @Override
    public String toString() {
        return "AddCollectInfoReq{" +
                "userId=" + userId +
                ", driverId=" + driverId +
                ", driverNote=" + driverNote +
                ", city=" + city +
                ", driverCity=" + driverCity +
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDriverCity() {
        return driverCity;
    }

    public void setDriverCity(String driverCity) {
        this.driverCity = driverCity;
    }
}
