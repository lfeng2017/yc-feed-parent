package com.yc.feed.domain.req;

/**
 * Created by yusong on 2016/11/2.
 * 分页公共参数
 */
public class PageReq {
    //分页参数
    private Long pageNum;
    private Long pageSize;
    //只返回数目
    private boolean beCount;
    //根据pageNum 和 pageSize组sql
    private String pageSql;

    //时间格式改为时间戳
    private Long startTimeStamp ;
    private Long endTimeStamp ;


    @Override
    public String toString() {
        return "PageReq{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", beCount=" + beCount +
                ", pageSql='" + getPageSql() + '\'' +
                ", startTimeStamp=" + startTimeStamp +
                ", endTimeStamp=" + endTimeStamp +
                '}';
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isBeCount() {
        return beCount;
    }

    public void setBeCount(boolean beCount) {
        this.beCount = beCount;
    }

    public Long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(Long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(Long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getPageSql() {
        if(null == pageNum ||  null == pageSize){
            return null;
        }
        long pageStartNum = 0;
        pageStartNum = (pageNum - 1) * pageSize;
        pageSql = " limit " + String.valueOf(pageStartNum) + "," + String.valueOf(pageSize);
        return pageSql;
    }
}
