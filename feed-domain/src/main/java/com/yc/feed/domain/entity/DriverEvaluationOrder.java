package com.yc.feed.domain.entity;


/*
*司机最近评价50单信息
*/
public class DriverEvaluationOrder {
    private Integer driverEvaluationOrderId;

    private Long driverId;

    private String recentOrderString;

    private Integer createTime;

    private Integer updateTime;

    @Override
    public String toString() {
        return "DriverEvaluationOrder{" +
                "driverEvaluationOrderId=" + driverEvaluationOrderId +
                ", driverId=" + driverId +
                ", recentOrderString='" + recentOrderString + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Integer getDriverEvaluationOrderId() {
        return driverEvaluationOrderId;
    }

    public void setDriverEvaluationOrderId(Integer driverEvaluationOrderId) {
        this.driverEvaluationOrderId = driverEvaluationOrderId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getRecentOrderString() {
        return recentOrderString;
    }

    public void setRecentOrderString(String recentOrderString) {
        this.recentOrderString = recentOrderString == null ? null : recentOrderString.trim();
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}