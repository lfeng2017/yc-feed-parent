package com.yc.feed.api.http.req;

import com.yc.feed.domain.req.PageReq;

/**
 * Created by yusong on 2016/10/26.
 * 获取黑名单请求参数
 */
public class GetBlackListReq extends PageReq{
    //根据Id
    private Long blackListId;
    //用户ID
    private Long userId;
    //司机ID
    private Long driverId;
    //黑名单类型
    private Integer type;
    //是否只返回记录数目
    private boolean beCount;

    @Override
    public String toString() {
        return
                super.toString()+
                "GetBlackListReq{" +
                "blackListId=" + blackListId +
                "userId=" + userId +
                ", driverId=" + driverId +
                ", type=" + type +
                ", beCount=" + beCount +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isBeCount() {
        return beCount;
    }

    public void setBeCount(boolean beCount) {
        this.beCount = beCount;
    }

    public Long getBlackListId() {
        return blackListId;
    }

    public void setBlackListId(Long blackListId) {
        this.blackListId = blackListId;
    }
}
