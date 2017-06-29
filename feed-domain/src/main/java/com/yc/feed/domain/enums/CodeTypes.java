package com.yc.feed.domain.enums;

/**
 * Created by yusong on 2016/10/14.
 * 返回码
 */
public enum CodeTypes {
    Success(200,"成功"),
    RepeatError(300,"订单重复"),
    ParaError(400,"parameter error"),
    SystemError(500,"系统异常"),
    NoRecord(404,"未找到记录"),
    Conflict(409,"逻辑冲突"),
    Conflict4BlackList(405,"用户已经收藏该司机，不能加入到黑名单"),
    ;

    CodeTypes(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }
    public String getCodeStr() {
        return String.valueOf(code);
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
