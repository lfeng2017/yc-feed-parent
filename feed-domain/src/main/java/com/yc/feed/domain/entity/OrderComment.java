package com.yc.feed.domain.entity;

public class OrderComment {
    //ID
    private Integer orderCommentId;
    //订单ID
    private Long serviceOrderId;
    //订车用户ID
    private Integer userId;
    //订单产品类型ID
    private Integer productTypeId;
    //订单产品类型ID
    private Integer carTypeId;
    //城市
    private String city;
    //司机ID
    private Integer driverId;
    //百分制分数
    private Integer score;
    //评价内容
    private String content;

    private Integer commentUserId;

    private Integer createTime;

    private Integer updateTime;

    private Integer operatorId;

    private Byte status;
    //订单结束时间
    private Integer endTime;

    private Byte displayStatus;

    private Integer score1;

    private Integer score2;

    private Integer score3;

    private Integer score4;

    private Integer score5;
    //评价 1:好评 0:中评 -1:差评
    private Byte evaluation;

    @Override
    public String toString() {
        return "OrderComment{" +
                "orderCommentId=" + orderCommentId +
                ", serviceOrderId=" + serviceOrderId +
                ", userId=" + userId +
                ", productTypeId=" + productTypeId +
                ", carTypeId=" + carTypeId +
                ", city='" + city + '\'' +
                ", driverId=" + driverId +
                ", score=" + score +
                ", content='" + content + '\'' +
                ", commentUserId=" + commentUserId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorId=" + operatorId +
                ", status=" + status +
                ", endTime=" + endTime +
                ", displayStatus=" + displayStatus +
                ", score1=" + score1 +
                ", score2=" + score2 +
                ", score3=" + score3 +
                ", score4=" + score4 +
                ", score5=" + score5 +
                ", evaluation=" + evaluation +
                '}';
    }

    public Integer getOrderCommentId() {
        return orderCommentId;
    }

    public void setOrderCommentId(Integer orderCommentId) {
        this.orderCommentId = orderCommentId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Integer carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Byte getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Byte displayStatus) {
        this.displayStatus = displayStatus;
    }

    public Integer getScore1() {
        return score1;
    }

    public void setScore1(Integer score1) {
        this.score1 = score1;
    }

    public Integer getScore2() {
        return score2;
    }

    public void setScore2(Integer score2) {
        this.score2 = score2;
    }

    public Integer getScore3() {
        return score3;
    }

    public void setScore3(Integer score3) {
        this.score3 = score3;
    }

    public Integer getScore4() {
        return score4;
    }

    public void setScore4(Integer score4) {
        this.score4 = score4;
    }

    public Integer getScore5() {
        return score5;
    }

    public void setScore5(Integer score5) {
        this.score5 = score5;
    }

    public Byte getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Byte evaluation) {
        this.evaluation = evaluation;
    }
}