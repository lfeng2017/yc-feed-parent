package com.yc.feed.domain.req;

/**
 * Created by yusong on 2016/11/1.
 * 新增黑名单参数
 */
public class AddBlackListReq {
    //用户ID
    private Long userId;
    //司机ID
    private Long driverId;
    //黑名单类型
    private Integer type;
    //订单号
    private Long serviceOrderId;

    @Override
    public String toString() {
        return "AddBlackListReq{" +
                "userId=" + userId +
                ", driverId=" + driverId +
                ", type=" + type +
                ", serviceOrderId=" + serviceOrderId +
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }
}
