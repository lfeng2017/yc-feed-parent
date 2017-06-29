package com.yc.feed.service.listener;

import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.OrderMQ;
import com.yc.feed.domain.req.UpdateServiceTimesReq;
import com.yc.feed.service.CollectInfoService;
import com.yc.feed.service.PlatformService;
import com.yongche.consumer.rabbitmq.utils.phprpc.PHPSerializerUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lfeng on 2017/1/20.
 */
public class DriverServiceCountListener {
    private final Logger logger = Logger.getLogger(DriverServiceCountListener.class);
    @Autowired
    private CollectInfoService collectInfoService;
    @Autowired
    private PlatformService platformService;

    public void onMessage(String msg) {
        long startTime = System.currentTimeMillis();
        PHPSerializerUtil phpSerializer = new PHPSerializerUtil();
        OrderMQ orderMQ = null;
        try {
            orderMQ = (OrderMQ)phpSerializer.unserialize(msg.getBytes(),OrderMQ.class);
            logger.info("onMessage|解析Ok|"+orderMQ);
        } catch (Exception e) {
            logger.error("onMessage|解析错误|"+msg,e);
            return;
        }
        if(orderMQ != null){
            if ("order_q_7".equals(orderMQ.get__KEY__()) && orderMQ.getUser_id()!=null && orderMQ.getDriver_id()!=null){
                int serviceCount = platformService.getDriverServiceCount(orderMQ.getDriver_id(),orderMQ.getUser_id());
                UpdateServiceTimesReq updateServiceTimesReq = new UpdateServiceTimesReq();
                updateServiceTimesReq.setDriverId(orderMQ.getDriver_id());
                updateServiceTimesReq.setUserId(orderMQ.getUser_id());
                updateServiceTimesReq.setServiceTimes(serviceCount);
                try {
                    collectInfoService.updateServiceTimes(updateServiceTimesReq);
                    logger.info("onMessage|"+(System.currentTimeMillis()-startTime)+"ms|success");
                } catch (FeedException e) {
                    logger.error("onMessage|updateServiceTimes|ERROR|"+e.getMessage());
                }
            }
        }
    }
}
