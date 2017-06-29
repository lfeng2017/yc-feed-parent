package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/11/25.
 * 添加司机评价用户标签
 */
public class WebAddJudgementTagReq {
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
    //类型 1:正面 -1:负面
    private Byte type;

    @Override
    public String toString() {
        return "WebAddJudgementTagReq{" +
                "orderId=" + serviceOrderId +
                ", driverId=" + driverId +
                ", userId=" + userId +
                ", commentUserTagId=" + commentUserTagId +
                ", tagText='" + tagText + '\'' +
                ", type=" + type +
                '}';
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
        this.tagText = tagText;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
