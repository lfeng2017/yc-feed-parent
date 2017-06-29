package com.yc.feed.domain.excep;

/**
 * Created by yusong on 2016/10/22.
 * 系统异常
 */
public class FeedException extends Throwable{

    //返回码 0000为成功，其他为失败
    private int code;
    //描述信息
    private String msg;

    public FeedException(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "FeedException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
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
