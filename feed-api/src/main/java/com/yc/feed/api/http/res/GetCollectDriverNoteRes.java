package com.yc.feed.api.http.res;

/**
 * Created by ke on 2016/11/2.
 * 获取司机对乘客的评价返回结果
 */
public class GetCollectDriverNoteRes {
    //是否成功
    public boolean success;
    //返回码 200标识成功
    public int code;
    //描述
    public String msg;
    //司机对乘客的评价内容
    public String notes;


    public GetCollectDriverNoteRes(boolean success, int code, String msg, String notes) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.notes = notes;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
