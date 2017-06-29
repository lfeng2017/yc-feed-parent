package com.yc.feed.domain.enums;

/**
 * Created by yusong on 2016/11/8.
 *操作者ID
 */
public enum SystemOperatorIdType {
    MerchantPlatform(1)
    ;

    SystemOperatorIdType(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
