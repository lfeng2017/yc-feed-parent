package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/10/26.
 * 新增黑名单请求
 */
public class WebAddBlackListReq {

    //用户ID
    private String userId;
    //司机ID
    private String driverId;
    //黑名单类型  1用户拉黑司机，2司机拉黑用户
    private String type;
    //订单号
    private String serviceOrderId;

    @Override
    public String toString() {
        return "AddBlackListReq{" +
                "userId=" + userId +
                ", driverId=" + driverId +
                ", type='" + type + '\'' +
                ", serviceOrderId=" + serviceOrderId +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(String serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }
}
