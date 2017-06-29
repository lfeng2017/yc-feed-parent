package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/10/26.
 * 分页获取黑名单请求
 */
public class GetBlackListByPageReq extends GetBlackListReq {
    //获取数量
    private long limit;
    //offset
    private long offset;

    @Override
    public String toString() {
        return "GetBlackListByPageReq{" +
                "userId='" + getUserId() + '\'' +
                ", driverId='" + getDriverId() + '\'' +
                ", type='" + getType() + '\'' +
                "limit=" + limit +
                ", offset=" + offset +
                '}';
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
