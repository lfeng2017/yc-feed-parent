package com.yc.feed.domain.req;

/**
 * Created by yusong on 2016/11/14.
 * 撤销收藏请求
 */
public class WithdrawCollectReq {

    //需要撤销的记录ID
    private Long collectId;
    //用户ID
    private String userId;
    //司机ID
    private String driverId;

    @Override
    public String toString() {
        return "WithdrawCollectReq{" +
                "userId='" + userId + '\'' +
                ", driverId='" + driverId + '\'' +
                "collectId=" + collectId +
                '}';
    }

    public Long getCollectId() {
        return collectId;
    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
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
}
