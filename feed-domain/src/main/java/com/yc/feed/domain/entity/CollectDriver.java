package com.yc.feed.domain.entity;

import java.util.Arrays;

public class CollectDriver {
    //记录ID
    private Long collectDriverId;
    //用户ID
    private Long userId;
    //司机ID
    private Long driverId;
    //创建时间戳
    private Long createTime;
    //更新时间
    private Long updateTime;
    //服务次数
    private Integer serviceTimes;
    //记录状态  1:收藏，2：撤销收藏
    private byte status;
    //司机备注
    private String driverNote;
    //司机所在城市
    private byte[] driverCity;

    @Override
    public String toString() {
        return "CollectDriver{" +
                "collectDriverId=" + collectDriverId +
                ", userId=" + userId +
                ", driverId=" + driverId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", serviceTimes=" + serviceTimes +
                ", status=" + status +
                ", driverNote='" + driverNote + '\'' +
                ", driverCity=" + Arrays.toString(driverCity) +
                '}';
    }

    public Long getCollectDriverId() {
        return collectDriverId;
    }

    public void setCollectDriverId(Long collectDriverId) {
        this.collectDriverId = collectDriverId;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(Integer serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getDriverNote() {
        return driverNote;
    }

    public void setDriverNote(String driverNote) {
        this.driverNote = driverNote == null ? null : driverNote.trim();
    }

    public byte[] getDriverCity() {
        return driverCity;
    }

    public void setDriverCity(byte[] driverCity) {
        this.driverCity = driverCity;
    }
}