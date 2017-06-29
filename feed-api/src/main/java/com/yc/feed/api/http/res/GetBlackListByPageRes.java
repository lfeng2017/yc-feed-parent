package com.yc.feed.api.http.res;



import com.yc.feed.domain.entity.BlackList;

import java.util.List;

/**
 * Created by yusong on 2016/10/29.
 * 获取黑名单
 */
public class GetBlackListByPageRes extends CommonRes {
    //黑名单总数（不考虑分页）
    private long count;
    //黑名单列表
    private List<BlackList> blackList;

    public GetBlackListByPageRes(boolean success, int code, String msg) {
        super(success,code+"",msg);
    }

    public GetBlackListByPageRes(boolean success,String code, String msg,long count, List<BlackList> blackList) {
        super();
        super.setSuccess(success);
        super.setCode(code);
        super.setMsg(msg);
        this.count = count;
        this.blackList = blackList;
    }

    @Override
    public String toString() {
        return
                super.toString()+
                "GetBlackListByPageRes{" +
                "count=" + count +
                ", blackList Size=" + ( blackList == null ? 0: blackList.size()  ) +
                '}';
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<BlackList> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<BlackList> blackList) {
        this.blackList = blackList;
    }
}
