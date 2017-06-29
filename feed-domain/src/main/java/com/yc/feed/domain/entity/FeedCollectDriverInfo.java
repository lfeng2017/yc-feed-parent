package com.yc.feed.domain.entity;

/**
 * Created by ke on 16-11-1.
 */
public class FeedCollectDriverInfo {

    private long collectDriverId;
    private long userId;
    private long driverId;
    private long createTime;
    private long updateTime;
    private int serviceTimes;
    private int status;
    private String driverNote;
    private String city;
    private String driverCity;

    public long getCollectDriverId() {
        return collectDriverId;
    }

    public void setCollectDriverId(long collectDriverId) {
        this.collectDriverId = collectDriverId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(int serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
