package com.yc.feed.domain.model;

/**
 * Created by yusong on 2016/11/15.
 * 司机标签统计
 */
public class DriverTagStatModel {
    //司机ID
    private Long driverId;
    //标签评价次数
    private Integer count;
    //标签信息
    private CommentTagModel commentTagModel;

    @Override
    public String toString() {
        return "DriverTagStatModel{" +
                "driverId=" + driverId +
                ", count=" + count +
                ", commentTagModel=" + commentTagModel +
                '}';
    }

    public DriverTagStatModel(Long driverId, Integer count, CommentTagModel commentTagModel) {
        this.driverId = driverId;
        this.count = count;
        this.commentTagModel = commentTagModel;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public CommentTagModel getCommentTagModel() {
        return commentTagModel;
    }

    public void setCommentTagModel(CommentTagModel commentTagModel) {
        this.commentTagModel = commentTagModel;
    }
}
