package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/10/28.
 * 添加司机对用户评价请求
 */
public class WebAddJudgementReq {
    //订单号
    private String orderId;
    //用户id
    private String userId;
    //司机id
    private String driverId;
    //分数
    private String score;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
