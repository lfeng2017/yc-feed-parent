package com.yc.feed.api.http.res;

/**
 * Created by yusong on 2016/10/17.
 * 司机好评率返回
 */
public class EvaluationRateRes {
    //是否成功
    public boolean success;
    //返回码 200标识成功
    public String code;
    //描述
    public String msg;
    //订单ID
    public long orderId;
    //好评率 百分比
    public int rate;

    public EvaluationRateRes(boolean success, String code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public EvaluationRateRes(boolean success, String code, String msg, long orderId, int rate) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.orderId = orderId;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "EvaluationRateRes{" +
                "success=" + success +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", orderId=" + orderId +
                ", rate=" + rate +
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

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
