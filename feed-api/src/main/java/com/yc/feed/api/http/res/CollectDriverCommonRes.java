package com.yc.feed.api.http.res;

/**
 * Created by ke on 2016/11/2.
 * 编辑司机对乘客的评价返回结果
 */
public class CollectDriverCommonRes {
    //是否成功
    public boolean success;
    //返回码 200标识成功
    public int code;
    //描述
    public String msg;

    public CollectDriverCommonRes(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CollectDriverCommonRes{" +
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
