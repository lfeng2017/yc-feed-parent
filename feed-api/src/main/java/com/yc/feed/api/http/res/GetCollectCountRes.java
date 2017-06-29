package com.yc.feed.api.http.res;

import java.util.List;

/**
 * Created by ke on 16-11-2.
 */
public class GetCollectCountRes {

    //是否成功
    public boolean success;
    //返回码 200标识成功
    public int code;
    //描述
    public String msg;
    //计数
    private int count;


    public GetCollectCountRes(boolean success, int code, String msg, int count) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.count = count;
    }
    @Override
    public String toString() {
        return "GetCollectCountRes{" +
                "success=" + success +
                "code=" + code +
                "msg=" + msg +
                "count=" + count +
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }






}
