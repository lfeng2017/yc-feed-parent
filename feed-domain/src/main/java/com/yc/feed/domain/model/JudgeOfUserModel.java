package com.yc.feed.domain.model;

/**
 * Created by yusong on 2016/11/17.
 * 司机评价乘客
 */
public class JudgeOfUserModel {
    //ID
    private Long id;
    //订单号
    private Long serviceOrderId;
    //司机Id
    private Long driverId;
    //用户ID
    private Long userId;
    //分数
    private Integer score;
    //评价内容
    private String detail;
    //创建时间戳
    private Long createTime;

    @Override
    public String toString() {
        return "JudgeOfUserModel{" +
                "id=" + id +
                ", serviceOrderId=" + serviceOrderId +
                ", driverId=" + driverId +
                ", userId=" + userId +
                ", score=" + score +
                ", detail='" + detail + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
