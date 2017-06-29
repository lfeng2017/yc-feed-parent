package com.yc.feed.domain.req;

/**
 * Created by yusong on 2016/11/17.
 * 查询司机评价用户
 */
public class GetJudgementReq extends PageReq {

    //司机ID
    private Long driverId;
    //用户ID
    private Long userId;
    //订单ID
    private Long serviceOrderId;

    @Override
    public String toString() {
        return super.toString()+
                "GetJudgementReq{" +
                "driverId=" + driverId +
                ", userId=" + userId +
                ", serviceOrderId=" + serviceOrderId +
                '}';
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }
}
