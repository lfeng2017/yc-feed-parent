package com.yc.feed.domain.model;

/**
 * Created by lfeng on 2017/2/13.
 */
public class DriverRecentOrderComment {
    //司机ID
    private long driverId;
    //最近50单评价
    private String orders;

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }
}
