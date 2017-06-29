package com.yc.feed.domain.model;

/**
 * Created by yusong on 2016/11/18.
 * 订单消息
 * https://wiki.yongche.org/projects/tech/wiki/Order_center_q_wiki
 */
public class OrderMQ {

    private String __KEY__;
    //订单号
    private Long order_id;
    //司机ID
    private Long driver_id;
    //用户ID
    private Long user_id;
    //订单结束时间
    private Long end_time;
    //来源
    private String source;
    //订单状态
    private Integer status;
    //订单状态
    private String PAY_STATUS;
    //创建时间
    private Long create_time;

    @Override
    public String toString() {
        return "OrderMQ{" +
                "__KEY__='" + __KEY__ + '\'' +
                ", order_id=" + order_id +
                ", driver_id=" + driver_id +
                ", user_id=" + user_id +
                ", end_time=" + end_time +
                ", source='" + source + '\'' +
                ", status=" + status +
                ", PAY_STATUS='" + PAY_STATUS + '\'' +
                ", create_time=" + create_time +
                '}';
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPAY_STATUS() {
        return PAY_STATUS;
    }

    public void setPAY_STATUS(String PAY_STATUS) {
        this.PAY_STATUS = PAY_STATUS;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(Long driver_id) {
        this.driver_id = driver_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public String get__KEY__() {
        return __KEY__;
    }

    public void set__KEY__(String __KEY__) {
        this.__KEY__ = __KEY__;
    }
}
