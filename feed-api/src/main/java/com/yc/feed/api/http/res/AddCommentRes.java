package com.yc.feed.api.http.res;

/**
 * Created by yusong on 2016/10/13.
 * 添加用户评论响应
 */
public class AddCommentRes {
    //是否成功
    private boolean success;
    //返回码 0000为成功，其他为失败
    private int ret_code;
    //描述信息
    private String ret_msg;

    public AddCommentRes() {
    }

    public AddCommentRes(boolean success, int code, String msg) {
        this.success = success;
        this.ret_code = code;
        this.ret_msg = msg;
    }

    @Override
    public String toString() {
        return "AddCommentRes{" +
                "success=" + success +
                ", ret_code='" + ret_code + '\'' +
                ", ret_msg='" + ret_msg + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
