package com.yc.feed.domain.enums;

/**
 * Created by yusong on 2016/10/9.
 * 用户评价枚举
 */
public enum ValidateTypes {
    Positive((byte)1),
    Negative((byte)-1),
    Mediocrity((byte)0)
    ;
    ValidateTypes(byte typeCode) {
        this.typeCode = typeCode;
    }

    private byte typeCode;


    public static ValidateTypes getType(int code){
        if(code == 1){
            return Positive;
        }else if (code == -1){
            return Negative;
        }else{
            return Mediocrity;
        }
    }

    public byte getTypeCode() {
        return typeCode;
    }
}
