package com.yc.feed.domain.enums;

/**
 * Created by yusong on 2016/11/21.
 * 临时订单表状态
 */
public enum TempOrderStatus {
    OrderComplete(1),
    UserComment(2),
    DefaultComment(3),
    ;

    TempOrderStatus(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
