package com.yc.feed.api.http.req;

import com.yc.feed.domain.req.PageReq;

/**
 * Created by yusong on 2016/10/27.
 * 获取收藏指定司机的用户请求
 */
public class GetCollectInfoReq extends PageReq{
    //司机id
    private Long driverId;
    //用户id
    private Long userId;

    @Override
    public String toString() {
        return super.toString()+
                "GetCollectInfoReq{" +
                "driverId='" + driverId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public GetCollectInfoReq() {
        super();
    }

    public GetCollectInfoReq(Long driverId, Long userId) {
        super();
        this.driverId = driverId;
        this.userId = userId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
