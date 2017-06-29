package com.yc.feed.api.http.res;

import java.util.Arrays;

/**
 * Created by yusong on 2016/11/24.
 * 查询最近50单订单号返回结果
 */
public class Last50ordersRes extends CommonRes {
    //司机ID
    private Long driverId;
    //数量
    private int cnt;
    //订单号
    private String[] result;

    public Last50ordersRes(boolean success, String code, String msg) {
        super(success, code, msg);
    }

    public Last50ordersRes(boolean success, String code, String msg, Long driverId, String[] result) {
        super(success, code, msg);
        this.driverId = driverId;
        this.result = result;
    }

    @Override
    public String toString() {
        return  super.toString()+
                "Last50ordersRes{" +
                "driverId=" + driverId +
                ", cnt=" + cnt +
                ", result=" + Arrays.toString(result) +
                '}';
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public int getCnt() {
        if(null == result){
            return 0;
        }
        return result.length;
    }

    public String[] getResult() {
        return result;
    }

    public void setResult(String[] result) {
        this.result = result;
    }
}
