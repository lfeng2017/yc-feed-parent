package com.yc.feed.domain.req;

import java.util.Date;

/**
 * Created by yusong on 2016/11/8.
 * 获取订单评价列表
 */
public class GetCommentListReq extends PageReq{
    //司机ID
    private Long driverId;
    //用户ID
    private Long userId;

    //content
    private String content;
    //city
    private String city;
    //carTypeId
    private Integer carTypeId;
    //orderIds(逗号隔开的多个订单号)
    private String orderIds;

    //评价 1:好评 0:中评 -1:差评
    private Integer evaluation;
    //开始时间 格式 yyyy-MM-dd HH:mm:ss
    private Date startTime;
    //结束时间 格式 yyyy-MM-dd HH:mm:ss
    private Date endTime;
    //时间戳 秒级
    private Long startTimeStamp;
    private Long endTimeStamp;

    //标签ID
    private Integer tagId;

    private Integer status;

    private Integer displayStatus;

    private boolean descFlag;

    //是否需要标签信息
    private boolean needTag;


    @Override
    public String toString() {
        return "GetCommentListReq{" +
                "driverId=" + driverId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", city='" + city + '\'' +
                ", carTypeId=" + carTypeId +
                ", orderIds='" + orderIds + '\'' +
                ", evaluation=" + evaluation +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startTimeStamp=" + startTimeStamp +
                ", endTimeStamp=" + endTimeStamp +
                ", tagId=" + tagId +
                ", status=" + status +
                ", displayStatus=" + displayStatus +
                ", descFlag=" + descFlag +
                ", needTag=" + needTag +
                '}';
    }

    public boolean isDescFlag() {
        return descFlag;
    }

    public void setDescFlag(boolean descFlag) {
        this.descFlag = descFlag;
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

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isNeedTag() {
        return needTag;
    }

    public void setNeedTag(boolean needTag) {
        this.needTag = needTag;
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
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Integer carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(Integer displayStatus) {
        this.displayStatus = displayStatus;
    }
/*
    //数据库查询用
    private long startTimeNum;
    //数据库查询用
    private long endTimeNum;

    public long getStartTimeNum() {
        if(null == startTime){
            return 0;
        }
        startTimeNum = DateUtil.getNumber(startTime);
        return startTimeNum;
    }

    public long getEndTimeNum() {
        if(null == endTime){
            return 0;
        }
        endTimeNum = DateUtil.getNumber(endTime);
        return endTimeNum;
    }*/
}
