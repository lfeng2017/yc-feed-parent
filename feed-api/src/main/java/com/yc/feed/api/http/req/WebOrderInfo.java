package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/10/13.
 * 订单信息
 */
public class WebOrderInfo {
    //订单ID
    private String orderId;
    //司机ID
    private String driverId;
    //城市
    private String city;
    //车型ID
    private String carTypeId;
    //产品类型ID
    private String productTypeId;
    //订单完成时间
    private String endTime;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
