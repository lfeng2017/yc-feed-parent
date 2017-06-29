package com.yc.feed.task;

import com.yc.feed.api.util.DateUtil;
import com.yc.feed.dao.crm.CompletedOrderMapper;
import com.yc.feed.domain.entity.CompletedOrder;
import com.yc.feed.domain.enums.TempOrderStatus;
import com.yc.feed.domain.enums.ValidateTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.lock.Lock;
import com.yc.feed.service.HistoryEvalRateService;
import com.yc.feed.service.LockService;
import com.yc.feed.service.PlatformService;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by yusong on 2016/11/21.
 * 订单评论任务
 */
public class OrderCommentTask {

    private final Logger logger = Logger.getLogger(OrderCommentTask.class);
    //分布式锁名字
    private final static String DIS_LOCK_NAME = "feed:comment:task:default:positive";

    @Resource
    private CompletedOrderMapper completedOrderMapper;

    @Autowired
    private LockService lockService;

    @Autowired
    private HistoryEvalRateService historyEvalRateService;

    @Autowired
    private PlatformService platformService;

    //超时小时数
    private int timeOutHour;

    /*
    *查询超时评价订单,设置为好评
    */
    public void defaultPositiveComment() {
        Lock lock = lockService.getLock(DIS_LOCK_NAME);
        if (!lock.tryLock(601)) {
            logger.warn("defaultPositiveComment|未获取到锁|");
            return;
        }
        long startTime =  System.currentTimeMillis();
        logger.info("defaultPositiveComment|Start|获取到锁|开始执行|");
        try {
            if( timeOutHour == 0){
                //默认时间
                timeOutHour = 24;
            }
            Integer now = DateUtil.getNumber(new Date());
            long minEndTime = (long)(now - timeOutHour*3600);
            List<CompletedOrder> records =  completedOrderMapper.list(minEndTime);
            if(null == records || records.size() == 0){
                logger.warn("defaultPositiveComment|无订单需要处理|");
                return;
            }
            logger.info("defaultPositiveComment|待处理订单数目|"+(records == null ? 0 : records.size()));
            for(CompletedOrder record: records){
                try {
                    updateRecord(record);
                    logger.info("defaultPositiveComment|Success|"+record.toStringSimple());
                } catch (FeedException e) {
                    logger.error("defaultPositiveComment|Error|"+record.toStringSimple(),e);
                }
            }
        } finally {
            lock.unlock();
            logger.info("defaultPositiveComment|release lock|");
        }
        logger.info("defaultPositiveComment|End|"+(System.currentTimeMillis()-startTime)+"ms|");
    }

    /*
    *更新状态并发送通知更新好评率
    */
    @Transactional
    private void updateRecord(CompletedOrder record) throws FeedException{
        record.setEvaluation((int) ValidateTypes.Positive.getTypeCode());
        record.setStatus(TempOrderStatus.DefaultComment.getCode());
        record.setUpdateDate(new Date());
        record.setRemark("Default Judged");
        long num = 0;
        logger.info("updateRecord|更新订单|"+record);
        try {
            num = completedOrderMapper.updateStatus(record);
            logger.info("updateRecord|更新订单ok|影响行数|"+num);
        } catch (Exception e) {
            logger.error("updateRecord|更新订单出错|",e);
        }
        if(num > 0){
            Long serviceOrderId = record.getServiceOrderId();
            historyEvalRateService.updateDriverRate(record.getDriverId(),serviceOrderId,ValidateTypes.getType(record.getEvaluation()),true);
            //更新订单标志位，让用户不能再评价订单
            logger.info("updateRecord|更新订单标志位|"+serviceOrderId);
            platformService.updateOrder(serviceOrderId,Boolean.TRUE);
        }else{
            logger.warn("updateRecord|执行期间|用户已评价|"+record);
        }
    }

    public int getTimeOutHour() {
        return timeOutHour;
    }

    public void setTimeOutHour(int timeOutHour) {
        this.timeOutHour = timeOutHour;
    }
}
