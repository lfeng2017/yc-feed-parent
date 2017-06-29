package com.yc.feed.domain.req;

import com.yc.feed.domain.model.OrderInfo;

import java.util.List;

/**
 * Created by yusong on 2016/10/13.
 * 添加用户评论请求
 */
public class AddCommentReq {
    //用户Id
    private int userId;
    //订单Id
    private long orderId;
    //用户评价
    private String content;
    //评价分数
    private short score;
    //评价标签列表
    private List<Integer> commentTagIds;
    //是否需要标签
    private boolean beNeedCommentTag;
    //订单信息
    private OrderInfo orderInfo;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public short getScore() {
        return score;
    }

    public void setScore(short score) {
        this.score = score;
    }

    public List<Integer> getCommentTagIds() {
        return commentTagIds;
    }

    public void setCommentTagIds(List<Integer> commentTagIds) {
        this.commentTagIds = commentTagIds;
    }

    public boolean isBeNeedCommentTag() {
        return beNeedCommentTag;
    }

    public void setBeNeedCommentTag(boolean beNeedCommentTag) {
        this.beNeedCommentTag = beNeedCommentTag;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
