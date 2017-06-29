package com.yc.feed.api.http.res;

/**
 * Created by yusong on 2016/11/22.
 * 司机历史好评率
 */
public class HisRateRes extends CommonRes {

    //最后一个订单ID
    public Long orderId;
    //好评率 百分比
    public Integer rate;
    //差评单数目
    private Integer badOrderNum;
    //好评单数目
    private Integer goodOrderNum;

    public HisRateRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    public HisRateRes(boolean success, String code, String msg, Long orderId, Integer rate, Integer badOrderNum, Integer goodOrderNum) {
        super(success, code, msg);
        this.orderId = orderId;
        this.rate = rate;
        this.badOrderNum = badOrderNum;
        this.goodOrderNum = goodOrderNum;
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getBadOrderNum() {
        return badOrderNum;
    }

    public void setBadOrderNum(Integer badOrderNum) {
        this.badOrderNum = badOrderNum;
    }

    public Integer getGoodOrderNum() {
        return goodOrderNum;
    }

    public void setGoodOrderNum(Integer goodOrderNum) {
        this.goodOrderNum = goodOrderNum;
    }
}
