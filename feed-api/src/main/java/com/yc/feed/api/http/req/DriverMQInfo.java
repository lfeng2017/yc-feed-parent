package com.yc.feed.api.http.req;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yusong on 2016/11/3.
 * 司机端MQ
 */
public class DriverMQInfo implements Serializable{
    //订单号
    private long service_order_id;
    //司机ID
    private long driver_id;
    //评价
    private int evaluation;
    //时间戳
    private long time;

    /*
    a:5:{s:7:"__KEY__";s:9:"order_q_8";s:16:"service_order_id";i:6348633459023286087;s:9:"driver_id";i:50062000;s:4:"time";i:1478168074;s:10:"evaluation";i:1;}
     */
    public Map<String,Object> getMap(){
        Map<String,Object> para = new HashMap<String, Object>();
        para.put("service_order_id",String.valueOf(service_order_id));
        para.put("driver_id",String.valueOf(driver_id));
        para.put("evaluation",evaluation);
        para.put("time",time);
        return para;
    }

    @Override
    public String toString() {
        return "DriverMQInfo{" +
                "service_order_id='" + service_order_id + '\'' +
                ", driver_id='" + driver_id + '\'' +
                ", evaluation=" + evaluation +
                ", time(now)=" + time +
                '}';
    }

    public long getService_order_id() {
        return service_order_id;
    }

    public void setService_order_id(long service_order_id) {
        this.service_order_id = service_order_id;
    }

    public long getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(long driver_id) {
        this.driver_id = driver_id;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
