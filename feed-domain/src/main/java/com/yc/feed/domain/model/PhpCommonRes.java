package com.yc.feed.domain.model;

/**
 * Created by yusong on 2016/10/20.
 * 易到PHP通用返回
 */
public class PhpCommonRes {

    //返回码
    private int ret_code;
    //返回描述
    private String ret_msg;

    @Override
    public String toString() {
        return "PhpCommonRes{" +
                "ret_code=" + ret_code +
                ", ret_msg='" + ret_msg + '\'' +
                '}';
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }
}
