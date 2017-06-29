package com.yc.feed.api.http.res;

import com.yc.feed.domain.model.OrderCommentInfo;

import java.util.List;

/**
 * Created by yusong on 2016/11/8.
 * 获取评价列表返回结果
 */
public class GetCommentListRes extends CommonRes {
    //符合条件记录数量(不考虑分页)
    private long total;
    //本次返回结果数量
    private int count;
    //评价列表
    private List<OrderCommentInfo> commentList;

    public GetCommentListRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    public GetCommentListRes(boolean success, String code, String msg,long total, int count, List<OrderCommentInfo> commentList) {
        super(success, code, msg);
        this.total= total;
        this.count = count;
        this.commentList = commentList;
    }

    public List<OrderCommentInfo> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<OrderCommentInfo> commentList) {
        this.commentList = commentList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
