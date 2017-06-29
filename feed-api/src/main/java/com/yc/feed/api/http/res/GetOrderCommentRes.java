package com.yc.feed.api.http.res;

import com.yc.feed.domain.entity.OrderComment;
import com.yc.feed.domain.model.CommentTagModel;
import com.yc.feed.domain.model.OrderCommentInfo;

import java.util.List;

/**
 * Created by yusong on 2016/11/2.
 * 获取评价信息响应
 */
public class GetOrderCommentRes extends CommonRes{
    private OrderCommentInfo orderComment;

    public GetOrderCommentRes(boolean success, String code, String msg, OrderCommentInfo orderComment) {
        super(success, code, msg);
        this.orderComment = orderComment;
    }

    public OrderCommentInfo getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(OrderCommentInfo orderComment) {
        this.orderComment = orderComment;
    }
}
