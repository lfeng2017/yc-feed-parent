package com.yc.feed.domain.model;

/**
 * Created by yusong on 2016/11/11.
 * 用户收藏司机信息
 */
public class CollectedDriverModel {
    //记录ID
    private Long id;
    //用户ID
    private Long userId;
    //司机ID
    private Long driverId;
    //司机备注
    private String driverNote;
    //城市
    private String city;
    //司机所在城市
    private String driverCity;
    //服务次数
    private Integer serviceTimes;
    //创建时间戳 (妙级)
    private Long createTimeStamp;
    //创建时间戳 (妙级)
    private Long updateTimeStamp;


    @Override
    public String toString() {
        return "CollectedDriverModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", driverId=" + driverId +
                ", driverNote='" + driverNote + '\'' +
                ", city='" + city + '\'' +
                ", driverCity='" + driverCity + '\'' +
                ", serviceTimes=" + serviceTimes +
                ", createTimeStamp=" + createTimeStamp +
                ", updateTimeStamp=" + updateTimeStamp +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(Integer serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public Long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Long getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Long updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
}
