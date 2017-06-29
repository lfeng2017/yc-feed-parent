package com.yc.feed.api.http.res;

import com.yc.feed.domain.model.DriverTagStatModel;

import java.util.List;

/**
 * Created by yusong on 2016/11/15.
 * 获取司机标签请求返回结果
 */
public class GetDriverTagStatRes extends CommonRes {
    //列表数量
    private int cnt;

    //返回结果列表
    List<DriverTagStatModel> result;


    public GetDriverTagStatRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    public GetDriverTagStatRes(boolean success, String code, String msg,int cnt,List<DriverTagStatModel> result) {
        super(success, code, msg);
        this.cnt = cnt;
        this.result = result;
    }

    public List<DriverTagStatModel> getResult() {
        return result;
    }

    public void setResult(List<DriverTagStatModel> result) {
        this.result = result;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
