package com.yc.feed.api.http.res;

/**
 * Created by lfeng on 16-12-14.
 */
public class GetDriverAvgScoreRes {

    //是否成功
    public boolean success;
    //返回码 200标识成功
    public int code;
    //描述
    public String msg;
    //司机id
    public long driverId;
    //平均分
    private int avgSocre;

    public GetDriverAvgScoreRes(boolean success, int code, String msg) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public GetDriverAvgScoreRes(boolean success, int code, String msg, long driverId, int avgSocre) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.driverId = driverId;
        this.avgSocre = avgSocre;
    }

    @Override
    public String toString() {
        return "GetCollectCountRes{" +
                "success=" + success +
                "code=" + code +
                "msg=" + msg +
                "driverId=" + driverId +
                "avgSocre=" + avgSocre +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public int getAvgSocre() {
        return avgSocre;
    }

    public void setAvgSocre(int avgSocre) {
        this.avgSocre = avgSocre;
    }
}
