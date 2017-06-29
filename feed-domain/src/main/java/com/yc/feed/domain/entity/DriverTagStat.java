package com.yc.feed.domain.entity;

public class DriverTagStat {
    private Long driverCommentTagStatisticsId;
    //司机ID
    private Long driverId;
    //评价标签ID
    private Integer commentTagId;
    //标签内容
    private String tagText;
    //类型 1:正面 -1:负面
    private Byte type;
    //标记次数
    private Integer count;

    private Integer createTime;

    public Long getDriverCommentTagStatisticsId() {
        return driverCommentTagStatisticsId;
    }

    public void setDriverCommentTagStatisticsId(Long driverCommentTagStatisticsId) {
        this.driverCommentTagStatisticsId = driverCommentTagStatisticsId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Integer getCommentTagId() {
        return commentTagId;
    }

    public void setCommentTagId(Integer commentTagId) {
        this.commentTagId = commentTagId;
    }

    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText == null ? null : tagText.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}