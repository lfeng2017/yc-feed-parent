package com.yc.feed.api.http.res;

import com.yc.feed.domain.entity.JudgeOfUser;
import com.yc.feed.domain.model.JudgeOfUserModel;

import java.util.List;

/**
 * Created by yusong on 2016/11/17.
 * 获取司机评价用户列表
 */
public class GetJudgementListRes extends PageRes {
    //列表
    private List<JudgeOfUserModel> result ;

    public GetJudgementListRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    public GetJudgementListRes(boolean success, String code, String msg, long total, Integer count, List<JudgeOfUserModel> result) {
        super(success, code, msg, total, count);
        this.result = result;
    }

    @Override
    public String toString() {
        return super.toString()+
                "GetJudgementListRes{" +
                "result=" + result +
                '}';
    }

    public List<JudgeOfUserModel> getResult() {
        return result;
    }

    public void setResult(List<JudgeOfUserModel> result) {
        this.result = result;
    }
}
