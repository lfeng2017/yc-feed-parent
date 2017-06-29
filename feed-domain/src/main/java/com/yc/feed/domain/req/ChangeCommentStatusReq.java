package com.yc.feed.domain.req;

/**
 * Created by yusong on 2016/11/8.
 * 使评价失效请求
 */
public class ChangeCommentStatusReq {

    //操作者ID
    private Integer operatorId;
    //司机ID
    private Integer driverId;
    //订单ID
    private Long orderId;
    //订单状态
    private boolean status;

    @Override
    public String toString() {
        return "ChangeCommentStatusReq{" +
                "operatorId=" + operatorId +
                ", driverId=" + driverId +
                ", orderId=" + orderId +
                ", status=" + status +
                '}';
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
