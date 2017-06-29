package com.yc.feed.domain.entity;

public class BlackList {
    //ID
    private Long blackListId;
    //用户ID
    private Long userId;
    //司机ID
    private Long driverId;
    //黑名单类型：1用户拉黑司机，2司机拉黑用户
    private Integer type;
    //创建时间
    private Long createTime;
    //订单号
    private Long serviceOrderId;

    @Override
    public String toString() {
        return "BlackList{" +
                "blackListId=" + blackListId +
                ", userId=" + userId +
                ", driverId=" + driverId +
                ", type=" + type +
                ", createTime=" + createTime +
                ", serviceOrderId=" + serviceOrderId +
                '}';
    }

    public Long getBlackListId() {
        return blackListId;
    }

    public void setBlackListId(Long blackListId) {
        this.blackListId = blackListId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }
}