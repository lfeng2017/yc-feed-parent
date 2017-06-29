package com.yc.feed.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yc.feed.api.http.res.HisRateRes;
import com.yc.feed.api.util.DateUtil;
import com.yc.feed.api.util.RedisKeyUtil;
import com.yc.feed.dao.crm.DriverEvaluationOrderMapper;
import com.yc.feed.dao.crm.DriverMapper;
import com.yc.feed.dao.mapper.OrderCommentMapper;
import com.yc.feed.domain.entity.Driver;
import com.yc.feed.domain.entity.DriverEvaluationOrder;
import com.yc.feed.domain.entity.OrderComment;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.enums.ValidateTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.DriverCommentEvaluationInfo;
import com.yc.feed.service.DriverRateService;
import com.yc.feed.service.HistoryEvalRateService;
import com.yc.feed.service.RedisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by yusong on 2016/11/23.
 * 历史好评服务实现
 */
@Service
public class HistoryEvalRateServiceImpl implements HistoryEvalRateService {

    private final Logger logger = Logger.getLogger(HistoryEvalRateServiceImpl.class);

    @Resource
    private DriverEvaluationOrderMapper driverEvaluationOrderMapper;

    @Resource
    private DriverMapper driverMapper;

    @Resource
    private OrderCommentMapper orderCommentMapper;

    @Autowired
    private RedisService<DriverCommentEvaluationInfo> redisService;

    @Autowired
    private DriverRateService driverRateService;


    @Override
    public HisRateRes getHisEvaluation(Long driverId) {
        /*DriverCommentEvaluationInfo driverCommentEvaluationInfo = getCache(driverId);
        if(null == driverCommentEvaluationInfo){*/
        logger.warn("getHisEvaluation|redis中未查询到好评率缓存,开始查询DB|driverId:"+driverId);
        DriverCommentEvaluationInfo driverCommentEvaluationInfo = getDbVal(driverId);
        /*redisService.put(RedisKeyUtil.getHisRateKey(driverId),driverCommentEvaluationInfo);*/
        /*}*/
        if (null == driverCommentEvaluationInfo){
            logger.error("driverCommentEvaluationInfo|未查询到记录|driverId:"+driverId);
            return new HisRateRes(Boolean.FALSE,CodeTypes.NoRecord.getCodeStr(),CodeTypes.NoRecord.getMsg());
        }
        return new HisRateRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),driverCommentEvaluationInfo.getLastOrderId(),driverCommentEvaluationInfo.getEvaluation(),driverCommentEvaluationInfo.getBadOrders(),driverCommentEvaluationInfo.getGoodOrders());
    }


    @Override
    public String[] getLastOrder(Long driverId) throws FeedException {
        DriverEvaluationOrder record = driverEvaluationOrderMapper.selectByDriverId(driverId);
        if(null == record){
            throw new FeedException(CodeTypes.NoRecord.getCode(),CodeTypes.NoRecord.getMsg());
        }
        String orderStr = record.getRecentOrderString();
        String[] orders = orderStr.split(",");
        return orders;
    }

    @Override
    public void updateRateByStatus(Long driverId, Long orderId, boolean status) {
        boolean needRefresh = status;
        if(!status){
            DriverEvaluationOrder record = driverEvaluationOrderMapper.selectByDriverId(driverId);
            if(record == null){
                logger.warn("updateRateByStatus|未查询到最近50单记录|driverId:"+driverId);
                needRefresh = true;
            }else{
                String orderStr = record.getRecentOrderString();
                if (orderStr.contains(String.valueOf(orderId))){
                    needRefresh = true;
                }else{
                    logger.info("updateRateByStatus|更新的订单不在50单中不处理|driverId:"+driverId+"|orderId:"+orderId);
                    needRefresh = false;
                }
            }
        }
        if(needRefresh){
            refreshRate(driverId);
        }
    }

    /*
            *获取第50单
            */
    private Long get50thOrder(Long driverId){
        logger.info("get50thOrder|自动评价|获取第50单");
        String[] orders = null;
        Long res = null;
        try {
            orders = getLastOrder(driverId);
        } catch (FeedException e) {
            logger.warn("get50thOrder|未找到用户订单|");
            return null;
        }
        if(null != orders && orders.length == 50){
            try {
                res = Long.parseLong(orders[49]);
            } catch (NumberFormatException e) {
                logger.warn("get50thOrder|格式错误|"+orders[49]);
            }
            return res;
        }
        return null;
    }

    @Override
    public void updateDriverRate(Long driverId, Long orderId, ValidateTypes val,boolean autoComment) {
        logger.info("updateDriverRate|开始更新司机端好评率|driverId:"+driverId+"|orderId:"+orderId+"|val:"+val);
        //1.更新最近50单
        Long firstOrderId = null;
        if(autoComment ){
            firstOrderId = get50thOrder(driverId);
            logger.info("updateDriverRate|自动评价|第50单:"+firstOrderId);
        }else{
            firstOrderId = update50orders(driverId,orderId);
            logger.info("updateDriverRate|用户评价|第50单:"+firstOrderId);
        }
        //2.查询第一单评价
        int firstOrderVal = 0;
        if(null != firstOrderId){
            OrderComment orderComment = orderCommentMapper.selectByOrderId(firstOrderId);
            logger.info("updateDriverRate|被压出的第一条评价信息|"+orderComment);
            if (null != orderComment){
                firstOrderVal = orderComment.getEvaluation();
            }else{
                logger.warn("updateDriverRate|根据订单ID未查询到记录|firstOrderId:"+firstOrderId);
            }
        }
        //3.更新redis 并获取好评率对象
        DriverCommentEvaluationInfo driverCommentEvaluationInfo = updateCache(driverId,firstOrderVal ,(int)val.getTypeCode(),orderId);
        //4.调用司机端接口 更新司机端好评率
       // boolean res = driverRateService.dealDriverRate(driverCommentEvaluationInfo);
        logger.info("updateDriverRate|更新司机端好评率结果|END");
    }


    /*
    *查询司机评价历史
    */
    private DriverCommentEvaluationInfo getDbVal(Long driverId){
        logger.info("getDbVal|查询司机信息|driverId:"+driverId);
        Driver driver = driverMapper.selectByPrimaryKey(driverId);
        logger.info("getDbVal|查询司机信息|driverId:"+driverId+"|driver:"+driver);
        if (driver == null) {
            logger.error("getDbVal|数据库中未查询到司机信息|driverId:"+driverId);
            return null;
        }
        DriverCommentEvaluationInfo info = new DriverCommentEvaluationInfo();
        info.setDriverId(driverId);
        info.setGoodOrders(driver.getGoodCommentCount());
        info.setBadOrders(driver.getBadCommentCount());
        logger.info("getDbVal|查询数据库历史好评记录|"+info);
        return info;
    }


    /*
    *更新缓存数据
    */
    private DriverCommentEvaluationInfo updateCache(Long driverId,int firstVal,int lastVal,Long orderId ){
        DriverCommentEvaluationInfo cachedData = getCache(driverId);
        if (cachedData == null){
            logger.warn("updateCache|redis中未查询到好评率缓存,开始查询DB|driverId:"+driverId);
            cachedData = getDbVal(driverId);
        }
        if (cachedData == null){
            logger.error("updateCache|DB中未查询到好评率数据|driverId:"+driverId);
            if (ValidateTypes.Positive.getTypeCode() == lastVal){
                cachedData = new DriverCommentEvaluationInfo(driverId,1,0,orderId);
            }else{
                cachedData = new DriverCommentEvaluationInfo(driverId,0,1,orderId);
            }
            redisService.put(RedisKeyUtil.getHisRateKey(driverId),cachedData);
            logger.info("updateCache|新缓存记录|"+cachedData);
            return cachedData;
        }
        int badNum = cachedData.getBadOrders();
        int goodNum = cachedData.getGoodOrders();
        if(badNum + goodNum >= 50){
            //好评单数+差评单数》50 才考虑压出的单
            int res = lastVal - firstVal;
            if(res > 0){
                goodNum = goodNum+1;
                badNum = badNum -1;
            }else if(res < 0){
                goodNum = goodNum-1;
                badNum = badNum+1;
            }
        }else{
            if(lastVal >= 0){
                goodNum = goodNum+1;
            }else if(lastVal < 0){
                badNum = badNum+1;
            }
        }
        cachedData.setBadOrders(badNum);
        cachedData.setGoodOrders(goodNum);
        cachedData.setLastOrderId(orderId);
        redisService.put(RedisKeyUtil.getHisRateKey(driverId),cachedData);
        logger.info("updateCache|更新缓存记录|"+cachedData);
        return cachedData;
    }

    /*
    *查询缓存数据
    */
    private DriverCommentEvaluationInfo getCache(Long driverId){
        String json = redisService.getStr(RedisKeyUtil.getHisRateKey(driverId));
        if(StringUtils.isEmpty(json)){
            logger.warn("getCache|redis中未查询到好评率|driverId:"+driverId);
            return null;
        }
        Gson gson = new Gson();
        DriverCommentEvaluationInfo driverCommentEvaluationInfo = null ;
        try {
            driverCommentEvaluationInfo = gson.fromJson(json,DriverCommentEvaluationInfo.class);
            logger.info("getCache|解析ok|"+driverCommentEvaluationInfo);
        } catch (JsonSyntaxException e) {
            logger.error("getCache|解析数据错误|"+json);
        }
        return driverCommentEvaluationInfo;
    }

    /*
    *更新最近50单并返回删除的一单的订单号ID
    * 记录格式：2005727978,2004268663,2004255933,2004256165,2004256341,2004256429,2004256507,2004250571
    */
    private Long update50orders(Long driverId,Long orderId){
        DriverEvaluationOrder record = driverEvaluationOrderMapper.selectByDriverId(driverId);
        if (null == record){
            //新增一条记录
            record = new DriverEvaluationOrder();
            record.setDriverId(driverId);
            record.setRecentOrderString(String.valueOf(orderId));
            record.setCreateTime(DateUtil.getNumber(new Date()));
            record.setUpdateTime(record.getCreateTime());
            logger.info("update50orders|新增一条DB缓存记录|"+record);
            driverEvaluationOrderMapper.insert(record);
            return null;
        }
        String orderStr = record.getRecentOrderString();
        String newOrderStr = null;
        Long exOrderId = null;
        if(StringUtils.isEmpty(orderStr)){
            newOrderStr = String.valueOf(orderId);
        }else{
            String[] orders = orderStr.split(",");
            if (orders.length >= 50){
                exOrderId = Long.parseLong(orders[orders.length-1]);
                newOrderStr = String.valueOf(orderId) +","+orderStr.substring(0,orderStr.lastIndexOf(','));
            }else{
                newOrderStr = String.valueOf(orderId)+","+orderStr;
            }
        }
        record.setUpdateTime(DateUtil.getNumber(new Date()));
        record.setRecentOrderString(newOrderStr);
        logger.info("update50orders|更新DB缓存记录|"+record);
        driverEvaluationOrderMapper.updateOrder(record);
        return exOrderId;
    }

    /*
    *根据评价记录重新计算用户好评率
    */
    private void refreshRate(Long driverId){
        logger.info("refreshRate|重新计算好评率|driverId:"+driverId);
        //1.重新计算好评率
        List<OrderComment> orders = orderCommentMapper.getLast50(driverId);
        logger.info("refreshRate|重新计算好评率|driverId:"+driverId+"|订单数量:"+(orders == null ? 0 : orders.size()));
        int badNum = 0;
        int goodNum = 0;
        StringBuffer sb = new StringBuffer();
        Long lastOrderId = null ;
        for(OrderComment order: orders){
            if(order.getStatus() ==1){
                goodNum++;
            }else if (order.getStatus() == -1 ){
                badNum ++;
            }
            lastOrderId = order.getServiceOrderId();
            sb.append(String.valueOf(lastOrderId) + ",");
        }
        String orderIds = sb.toString();
        if(!StringUtils.isEmpty(orderIds)){
            orderIds = orderIds.substring(0,orderIds.length()-1);
        }
        DriverCommentEvaluationInfo evaluationInfo = new DriverCommentEvaluationInfo(driverId,goodNum,badNum,lastOrderId);
        logger.info("refreshRate|重新计算好评率结果|"+evaluationInfo);
        //2.更新最近50单
        DriverEvaluationOrder record = driverEvaluationOrderMapper.selectByDriverId(driverId);
        if (null == record){
            //新增一条记录
            record = new DriverEvaluationOrder();
            record.setDriverId(driverId);
            record.setRecentOrderString(orderIds);
            record.setCreateTime(DateUtil.getNumber(new Date()));
            record.setUpdateTime(record.getCreateTime());
            logger.info("refreshRate|新增一条DB缓存记录|"+record);
            driverEvaluationOrderMapper.insert(record);
        }else{
            record.setRecentOrderString(orderIds);
            record.setUpdateTime(DateUtil.getNumber(new Date()));
            driverEvaluationOrderMapper.updateOrder(record);
            logger.info("refreshRate|修改一条DB缓存记录|"+record);
        }
        //3.更新缓存
        redisService.put(RedisKeyUtil.getHisRateKey(driverId),evaluationInfo);
        //4.更新司机端结果并发送MQ
        //boolean res = driverRateService.dealDriverRate(evaluationInfo);
        //logger.info("refreshRate|更新司机端好评率|driverId:"+driverId+"|res;"+res);
    }


/*    public static void main(String[] args){
        String orderStr = "111,222,333,444";
        String[] orders = orderStr.split(",");
        Long ex = Long.parseLong(orders[orders.length-1]);
        orderStr = "000" + ","+orderStr.substring(0,orderStr.lastIndexOf(','));
        System.out.println(ex);
        System.out.println(orderStr);
    }*/


}
