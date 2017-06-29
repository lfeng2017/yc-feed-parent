package com.yc.feed.domain.entity;

/*
*司机评价乘客标签
*/
public class OrderCommentUserTag {
    //ID
    private Integer orderCommentUserTagsId;
    //订单ID
    private Long serviceOrderId;
    //司机ID
    private Integer driverId;
    //用户ID
    private Integer userId;
    //标签ID
    private Integer commentUserTagId;
    //评价内容
    private String tagText;
    //创建时间
    private Integer createTime;
    //类型 1:正面 -1:负面
    private Byte type;

    @Override
    public String toString() {
        return "OrderCommentUserTag{" +
                "orderCommentUserTagsId=" + orderCommentUserTagsId +
                ", serviceOrderId=" + serviceOrderId +
                ", driverId=" + driverId +
                ", userId=" + userId +
                ", commentUserTagId=" + commentUserTagId +
                ", tagText='" + tagText + '\'' +
                ", createTime=" + createTime +
                ", type=" + type +
                '}';
    }

    public Integer getOrderCommentUserTagsId() {
        return orderCommentUserTagsId;
    }

    public void setOrderCommentUserTagsId(Integer orderCommentUserTagsId) {
        this.orderCommentUserTagsId = orderCommentUserTagsId;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCommentUserTagId() {
        return commentUserTagId;
    }

    public void setCommentUserTagId(Integer commentUserTagId) {
        this.commentUserTagId = commentUserTagId;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText == null ? null : tagText.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}