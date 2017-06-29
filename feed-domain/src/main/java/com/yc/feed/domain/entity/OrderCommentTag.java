package com.yc.feed.domain.entity;

public class OrderCommentTag {
    private Integer commentTagDetailId;

    private Long serviceOrderId;

    private Integer driverId;

    private Integer commentTagId;

    private String tagText;

    private Integer createTime;

    private Byte type;


    @Override
    public String toString() {
        return "OrderCommentTag{" +
                "commentTagDetailId=" + commentTagDetailId +
                ", serviceOrderId=" + serviceOrderId +
                ", driverId=" + driverId +
                ", commentTagId=" + commentTagId +
                ", tagText='" + tagText + '\'' +
                ", createTime=" + createTime +
                ", type=" + type +
                '}';
    }

    public Integer getCommentTagDetailId() {
        return commentTagDetailId;
    }

    public void setCommentTagDetailId(Integer commentTagDetailId) {
        this.commentTagDetailId = commentTagDetailId;
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

    public Integer getCommentTagId() {
        return commentTagId;
    }

    public void setCommentTagId(Integer commentTagId) {
        this.commentTagId = commentTagId;
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