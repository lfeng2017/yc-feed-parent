package com.yc.feed.api.http.req;

import java.io.Serializable;

/**
 * Created by yusong on 2016/10/8.
 * 队列中司机评价信息
 */
public class EvaluationInfo implements Serializable {

    private static final long serialVersionUID = -3961016444410256685L;

    //订单号
    private long orderId;

    //司机ID
    private long driverId;
    //司机评价 -1：差评 0： 中评  1：好评
    private int evaluation;
    //服务结束时间
    private long endTime;

    @Override
    public String toString() {
        return "EvaluationInfo{" +
                "orderId=" + orderId +
                ", driverId=" + driverId +
                ", evaluation=" + evaluation +
                ", endTime=" + endTime +
                '}';
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
