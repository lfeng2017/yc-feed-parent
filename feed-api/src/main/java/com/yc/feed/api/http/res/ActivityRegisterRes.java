package com.yc.feed.api.http.res;

/**
 * Created by yusong on 2016/10/18.
 */
public class ActivityRegisterRes {
    //是否成功
    public boolean success;
    //返回码 200标识成功
    public int code;
    //描述
    public String msg;

    public ActivityRegisterRes() {
    }

    public ActivityRegisterRes(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ActivityRegisterRes{" +
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
