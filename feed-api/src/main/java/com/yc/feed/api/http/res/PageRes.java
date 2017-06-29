package com.yc.feed.api.http.res;

/**
 * Created by yusong on 2016/11/11.
 * 分页返回结果
 */
public class PageRes extends CommonRes {

    //符合条件记录数量(不考虑分页)
    private long total;
    //本次返回结果数量
    private Integer count;

    public PageRes(boolean success, String code, String msg){
        super(success, code, msg);
    }

    public PageRes(boolean success, String code, String msg, long total, Integer count) {
        super(success, code, msg);
        this.total = total;
        this.count = count;
    }

    @Override
    public String toString() {
        return super.toString()+
                "PageRes{" +
                "total=" + total +
                ", count=" + count +
                '}';
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
