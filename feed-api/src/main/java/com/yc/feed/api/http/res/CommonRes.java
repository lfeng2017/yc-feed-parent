package com.yc.feed.api.http.res;

/**
 * Created by yusong on 2016/10/29.
 * 通用返回结果
 */
public class CommonRes {
    //是否成功
    public boolean success;
    //返回码 200标识成功
    public String code;
    //描述
    public String msg;

    public CommonRes() {
    }

    public CommonRes(boolean success, String code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CommonRes{" +
                "success=" + success +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
