package com.yc.feed.api.http.res;

import com.yc.feed.domain.model.CollectedDriverModel;

import java.util.Map;

/**
 * Created by yusong on 16-11-11.
 * 查询收藏司机返回结果
 */
public class GetCollectListRes extends PageRes{

    //司机端要求 count改为cnt
    private Integer cnt;

    //收藏list(用户/司机)or(司机/用户)
    private Map<String,CollectedDriverModel> result;

    public GetCollectListRes(boolean success, String code, String msg, long total, int count,Map<String,CollectedDriverModel> result) {
        super(success, code, msg, total, result.size());
        this.cnt = count;
        this.result = result;
    }

    public GetCollectListRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }


    public Map<String, CollectedDriverModel> getResult() {
        return result;
    }

    public void setResult(Map<String, CollectedDriverModel> result) {
        this.result = result;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
}
