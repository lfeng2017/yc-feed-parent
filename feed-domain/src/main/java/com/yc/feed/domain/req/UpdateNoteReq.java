package com.yc.feed.domain.req;

/**
 * Created by yusong on 2016/11/11.
 * 更新
 */
public class UpdateNoteReq {

    //记录ID
    private Long collectId;
    //司机备注
    private String driverNote;
    //用户Id
    private Long userId;
    //司机ID
    private Long driverId;


    @Override
    public String toString() {
        return "UpdateNoteReq{" +
                "collectId=" + collectId +
                ", driverNote='" + driverNote + '\'' +
                '}';
    }

    public Long getCollectId() {
        return collectId;
    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }

    public String getDriverNote() {
        return driverNote;
    }

    public void setDriverNote(String driverNote) {
        this.driverNote = driverNote;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
}
