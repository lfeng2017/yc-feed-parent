package com.yc.feed.domain.entity;

import java.util.Date;

public class CompletedOrder {
    private Integer id;
    //订单ID
    private Long serviceOrderId;
    //司机ID
    private Long driverId;
    //用户ID
    private Long userId;
    //服务结束时间
    private Long endTime;
    //评价 1:好评 0:暂无评价 -1:差评
    private Integer evaluation;
    //状态 1：订单完成，2：用户主动评价，3：24小时默认好评
    private Integer status;
    //备注
    private String remark;
    //创建时间
    private Date createDate;
    //更新时间
    private Date updateDate;

    @Override
    public String toString() {
        return "CompletedOrder{" +
                "id=" + id +
                ", serviceOrderId=" + serviceOrderId +
                ", driverId=" + driverId +
                ", userId=" + userId +
                ", endTime=" + endTime +
                ", evaluation=" + evaluation +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }

    public String toStringSimple() {
        return "CompletedOrder{" +
                "id=" + id +
                ", serviceOrderId=" + serviceOrderId +
                ", driverId=" + driverId +
                '}';
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}