package com.yc.feed.domain.model;

import java.util.Date;

/**
 * Created by yusong on 2016/10/13.
 * 订单信息
 */
public class OrderInfo {
    //订单ID
    private long orderId;
    //司机ID
    private int driverId;
    //城市
    private String city;
    //车型ID
    private int carTypeId;
    //产品类型ID
    private int productTypeId;
    //订单完成时间
    private int endTime;

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderId=" + orderId +
                ", driverId=" + driverId +
                ", city='" + city + '\'' +
                ", carTypeId=" + carTypeId +
                ", productTypeId=" + productTypeId +
                ", endTime=" + endTime +
                '}';
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(int carTypeId) {
        this.carTypeId = carTypeId;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
