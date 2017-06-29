package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/10/26.
 * 删除黑名单请求
 */
public class WebDelBlackListReq {
    private String blackListId;

    @Override
    public String toString() {
        return "WebDelBlackListReq{" +
                "blackListId='" + blackListId + '\'' +
                '}';
    }

    public String getBlackListId() {
        return blackListId;
    }

    public void setBlackListId(String blackListId) {
        this.blackListId = blackListId;
    }
}
