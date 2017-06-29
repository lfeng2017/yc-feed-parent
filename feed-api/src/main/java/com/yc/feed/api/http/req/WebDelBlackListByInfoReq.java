package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/10/26.
 * 删除黑名单请求
 */
public class WebDelBlackListByInfoReq {

    private String driverId;
    private String userId;

    @Override
    public String toString() {
        return "WebDelBlackListByInfoReq{" +
                "driverId='" + driverId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
