package com.yc.feed.api.http.req;

/**
 * Created by yusong on 2016/10/27.
 * 设置备注信息
 */
public class CollectDriverNoteReq {
    //司机ID
    private String driverId;
    //乘客ID
    private String userId;
    //备注信息
    private String note;

    @Override
    public String toString() {
        return "CollectDriverNoteReq{" +
                "driverId=" + driverId +
                ", userId=" + userId +
                ", note='" + note + '\'' +
                '}';
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
