package com.yc.feed.service.listener;

import com.yc.feed.dao.crm.CompletedOrderMapper;
import com.yc.feed.domain.entity.CompletedOrder;
import com.yc.feed.domain.enums.TempOrderStatus;
import com.yc.feed.domain.model.OrderMQ;
import com.yongche.consumer.rabbitmq.utils.phprpc.PHPSerializerUtil;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by yusong on 2016/11/18.
 * 订单MQ监听器
 * todo 测试自动创建QUEUE
 */
public class OrderMQListener {

    private final Logger logger = Logger.getLogger(OrderMQListener.class);

    @Resource
    private CompletedOrderMapper completedOrderMapper;


    /*
    *接收消息
    */
    public void listen(String msg) {
        logger.info("listen|收到订单MQ|"+msg);
        PHPSerializerUtil phpSerializer = new PHPSerializerUtil();
        OrderMQ orderMQ = null;
        try {
            orderMQ = (OrderMQ)phpSerializer.unserialize(msg.getBytes(),OrderMQ.class);
            logger.info("listen|解析Ok|"+orderMQ);
        } catch (ReflectiveOperationException e) {
            logger.error("listen|解析错误|"+msg,e);
            return;
        }
        if (7 == orderMQ.getStatus() && "order_q_pay_end".equals(orderMQ.get__KEY__())){
            insertRecord(orderMQ);
        }else{
            logger.info("listen|非支付成功订单|过滤|orderId:"+orderMQ.getOrder_id());
        }
    }

    /*
    *插入记录
    */
    public void insertRecord(OrderMQ orderMQ){
        CompletedOrder record = new CompletedOrder();
        record.setServiceOrderId(orderMQ.getOrder_id());
        record.setDriverId(orderMQ.getDriver_id());
        record.setUserId(orderMQ.getUser_id());
        record.setEndTime(orderMQ.getEnd_time());
        record.setEvaluation(0);
        record.setStatus(TempOrderStatus.OrderComplete.getCode());
        record.setRemark("");
        record.setCreateDate(new Date());
        record.setUpdateDate(record.getCreateDate());
        try {
            completedOrderMapper.insert(record);
            logger.info("insertRecord|插入订单临时记录ok|"+record);
        } catch (Exception e) {
            logger.info("insertRecord|插入订单临时记录错误|"+record,e);
        }
    }





}
