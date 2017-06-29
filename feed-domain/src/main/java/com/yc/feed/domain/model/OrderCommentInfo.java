package com.yc.feed.domain.model;

import java.util.List;

/**
 * Created by yusong on 2016/11/8.
 * 订单评价信息
 */
public class OrderCommentInfo {

    //ID
    private Integer orderCommentId;
    //订单ID
    private Long orderId;
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
    //订单结束时间
    private long endTimeStamp;
    //评价 1:好评 0:中评 -1:差评
    private Byte evaluation;
    //是否有效：默认有效1；0为无效
    private Integer status;

    //创建记录时间戳
    private Long createTimeStamp;
    //评价标签
    private List<CommentTagModel> tags;

    @Override
    public String toString() {
        return "OrderCommentInfo{" +
                "orderCommentId=" + orderCommentId +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", productTypeId=" + productTypeId +
                ", carTypeId=" + carTypeId +
                ", city='" + city + '\'' +
                ", driverId=" + driverId +
                ", score=" + score +
                ", content='" + content + '\'' +
                ", endTimeStamp=" + endTimeStamp +
                ", evaluation=" + evaluation +
                ", status=" + status +
                ", createTimeStamp=" + createTimeStamp +
                ", tags=" + tags +
                '}';
    }

    public Integer getOrderCommentId() {
        return orderCommentId;
    }

    public void setOrderCommentId(Integer orderCommentId) {
        this.orderCommentId = orderCommentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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
        this.city = city;
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
        this.content = content;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public Byte getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Byte evaluation) {
        this.evaluation = evaluation;
    }

    public List<CommentTagModel> getTags() {
        return tags;
    }

    public void setTags(List<CommentTagModel> tags) {
        this.tags = tags;
    }

    public Long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
