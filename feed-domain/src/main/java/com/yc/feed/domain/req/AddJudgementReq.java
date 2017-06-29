package com.yc.feed.domain.req;

/**
 * Created by yusong on 2016/10/28.
 * 添加司机对用户评价请求
 */
public class AddJudgementReq {
    //订单号
    private long orderId;
    //用户id
    private long userId;
    //司机id
    private long driverId;
    //分数
    private int score;
    //评价内容
    private String detail;

    @Override
    public String toString() {
        return "AddJudgementReq{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", driverId=" + driverId +
                ", score=" + score +
                ", detail='" + detail + '\'' +
                '}';
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
