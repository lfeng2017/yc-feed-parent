package com.yc.feed.api.http.req;

import java.util.List;

/**
 * Created by yusong on 2016/10/13.
 * 添加用户评论请求
 */
public class WebAddCommentReq {
    //用户Id
    private String userId;
    //订单Id
    private String orderId;
    //用户评价
    private String content;
    //评价分数
    private String score;
    //评价标签列表
    private List<String> commentTagIds;
    //是否需要标签
    private String beNeedCommentTag;
    //订单信息
    private WebOrderInfo orderInfo;

    @Override
    public String toString() {
        return "AddCommentReq{" +
                "userId=" + userId +
                ", orderId=" + orderId +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", commentTagIds=" + commentTagIds +
                ", beNeedCommentTag=" + beNeedCommentTag +
                ", orderInfo=" + orderInfo +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<String> getCommentTagIds() {
        return commentTagIds;
    }

    public void setCommentTagIds(List<String> commentTagIds) {
        this.commentTagIds = commentTagIds;
    }

    public String getBeNeedCommentTag() {
        return beNeedCommentTag;
    }

    public void setBeNeedCommentTag(String beNeedCommentTag) {
        this.beNeedCommentTag = beNeedCommentTag;
    }

    public WebOrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(WebOrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
