package com.yc.feed.service.impl;

import com.google.gson.Gson;
import com.le.config.client.ConfigManager;
import com.yc.feed.api.http.req.EvaluationInfo;
import com.yc.feed.api.util.HttpClientUtil;
import com.yc.feed.dao.crm.DriverEvaluationOrderMapper;
import com.yc.feed.dao.crm.DriverMapper;
import com.yc.feed.dao.crm.DriverTagStatMapper;
import com.yc.feed.dao.mapper.OrderCommentMapper;
import com.yc.feed.dao.mapper.OrderCommentTagMapper;
import com.yc.feed.domain.config.DictConfig;
import com.yc.feed.domain.entity.Driver;
import com.yc.feed.domain.entity.OrderComment;
import com.yc.feed.domain.enums.ValidateTypes;
import com.yc.feed.domain.model.DriverCommentEvaluationInfo;
import com.yc.feed.domain.req.AddCommentReq;
import com.yc.feed.service.DriverRateService;
import com.yc.feed.service.TagService;
import com.yongche.config.dict.Dicts;
import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lfeng on 2016/11/23.
 * 实现司机最近50单好评率结果处理
 */
@Service
public class DriverRateServiceImpl implements DriverRateService{
    private final Logger logger = Logger.getLogger(DriverRateServiceImpl.class);

    @Resource
    private AmqpTemplate driverRateTemplate;

    @Override
    public boolean dealDriverRate(DriverCommentEvaluationInfo driverRateResult) {
        long driverId =  driverRateResult.getDriverId();
        int rate = driverRateResult.getEvaluation();
        Map<String, Object> params =  new HashMap<String, Object>();
        params.put("driver_id",driverId);
        params.put("good_comment_rate",rate);
        params.put("good_comment_count",driverRateResult.getGoodOrders());
        params.put("bad_comment_count",driverRateResult.getBadOrders());
        String res = null;
        Gson gson = new Gson();
        logger.info("dealDriverRate|调用司机端|req:"+gson.toJson(params));
        try {
            String requestUrl = ConfigManager.get(DictConfig.class).getDictMap().get("driverRateUrl");
            res = HttpClientUtil.httpPostRequest(requestUrl,params);
            logger.info("dealDriverRate|调用司机端保存结果到库中|res:"+res);
        } catch (UnsupportedEncodingException e) {
            logger.error("request the driver rate interface error.driverId:"+driverRateResult.getDriverId(),e);
            return Boolean.FALSE;
        }
        Map<String,String> resultMap = gson.fromJson(res,Map.class);
        String  driverRateMqFlag = ConfigManager.get(DictConfig.class).getDictMap().get("driverRateMqFlag");
        if(("true").equals(driverRateMqFlag)){
            sendMQ(driverRateResult);
        }
        return "success".equals(resultMap.get("ret_msg"));
    }

    /*
     *发送司机最近50单好评率入MQ
     */
    private void sendMQ(DriverCommentEvaluationInfo driverRateResult){
        logger.info("sendMQ|发送司机最近50单好评率入MQ|START|"+driverRateResult);
        try {
            driverRateTemplate.convertAndSend(driverRateResult);
            logger.info("sendMQ|发送评价通知|END|"+driverRateResult);
        } catch (AmqpException e) {
            logger.error("sendMQ|发送评价通知错误|"+driverRateResult,e);
            throw e;
        }
    }

    @Resource
    private OrderCommentMapper orderCommentMapper;
    @Resource
    private DriverMapper driverMapper;
    @Override
    /*
     * 定时任务更新司机好评率
     */
    public void runDriverCommentEvaluation(int shardTotal,int currentShard) {
        long startTime =  System.currentTimeMillis();
        List<Long> allVailDriverids = driverMapper.getAllValidDriverIds(shardTotal,currentShard);
        logger.info("runDriverCommentEvaluation|获得有效的司机|Count:"+ allVailDriverids.size());
        Driver driver = null;
        for(Long driverId : allVailDriverids){
            try{
                List<OrderComment> orderComments = orderCommentMapper.getLast50(driverId);
                logger.info("runDriverCommentEvaluation|获得有效的driverid"+ driverId);
                int good =0;
                int bad =0;
                int rate =100;
                for(OrderComment orderComment : orderComments){
                    if(1 == orderComment.getEvaluation()){
                        good ++;
                    }else if(-1 == orderComment.getEvaluation()){
                        bad++;
                    }
                }

                if(bad+good != 0){
                    rate = ((good * 100)/(bad+good));
                }
                driver = new Driver();
                driver.setGoodCommentCount(good);
                driver.setBadCommentCount(bad);
                driver.setEvaluation(rate*10);
                driver.setGoodCommentRate(rate);
                driver.setDriverId((int)driverId.longValue());
                driverMapper.updateByPrimaryKeySelective(driver);
            }catch (Exception e){
                logger.error("司机ID"+driverId+"throw exception"+e.getMessage());
                continue;
            }
        }
        logger.info("runDriverCommentEvaluation|更新好评率完成|:"+(System.currentTimeMillis()-startTime)+"ms");
    }

}
