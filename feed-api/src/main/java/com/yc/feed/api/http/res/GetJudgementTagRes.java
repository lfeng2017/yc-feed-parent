package com.yc.feed.api.http.res;

import com.yc.feed.domain.entity.OrderCommentUserTag;

import java.util.List;

/**
 * Created by yusong on 2016/11/25.
 * 获取司机评价乘客标签
 */
public class GetJudgementTagRes extends CommonRes{
    //标签数量
    private int cnt;
    //标签接口
    private List<OrderCommentUserTag> res;


    public GetJudgementTagRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    public GetJudgementTagRes(boolean success, String code, String msg, int cnt, List<OrderCommentUserTag> res) {
        super(success, code, msg);
        this.cnt = cnt;
        this.res = res;
    }

    @Override
    public String toString() {
        return super.toString()+
                "GetJudgementTagRes{" +
                "cnt=" + cnt +
                ", res_size=" + (res == null?0:res.size()) +
                '}';
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<OrderCommentUserTag> getRes() {
        return res;
    }

    public void setRes(List<OrderCommentUserTag> res) {
        this.res = res;
    }
}
