package com.yc.feed.service.impl;


import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.le.config.client.ConfigManager;
import com.yc.feed.api.http.req.GetBlackListReq;
import com.yc.feed.api.http.req.GetCollectInfoReq;
import com.yc.feed.api.http.res.*;
import com.yc.feed.api.util.DateUtil;
import com.yc.feed.api.util.HttpClientUtil;
import com.yc.feed.dao.mapper.CollectDriverMapper;
import com.yc.feed.dao.shard.FeedCollectDriverMangoDao;
import com.yc.feed.domain.config.DictConfig;
import com.yc.feed.domain.entity.CollectDriver;
import com.yc.feed.domain.entity.CollectDriverInfo;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.CollectedDriverModel;
import com.yc.feed.domain.req.AddCollectDriverReq;
import com.yc.feed.domain.req.UpdateNoteReq;
import com.yc.feed.domain.req.UpdateServiceTimesReq;
import com.yc.feed.domain.req.WithdrawCollectReq;
import com.yc.feed.service.BlackListService;
import com.yc.feed.service.CollectInfoService;
import com.yc.feed.service.PlatformService;
import com.yc.feed.service.RedisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by ke on 16-11-1.
 * 评价与收藏服务实现
 */
@Service
public class CollectInfoServiceImpl implements CollectInfoService {
    private final Logger logger = Logger.getLogger(CollectInfoServiceImpl.class);

    @Resource
    private CollectDriverMapper collectDriverMapper;
    @Autowired
    private BlackListService blackListService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private RedisService<CollectDriver> redisService;
    //redis缓存1个月
    private static final int TIME_OUT = 30*24*60*60;

    private static final String COLLECTINFO_REDIS = "collectinfo:";
    //添加收藏成功发送通知文案
    private static final String NOTIFY_CONTENT = "有乘客刚刚收藏了您，快去看看吧！";


    @Override
    public GetCollectCountRes getUserCountByDriverId(long driverId) {
        logger.info("getUserCountByDriverId:"+driverId);
        GetCollectInfoReq req = new GetCollectInfoReq();
        req.setDriverId(driverId);
        return new GetCollectCountRes(Boolean.TRUE, CodeTypes.Success.getCode(), "success",
                collectDriverMapper.getCount(req));
    }

    @Override
    public GetCollectListRes getListByDriverId(GetCollectInfoReq getCollectInfoReq) {
        try{
            logger.info("getList|获取收藏司机列表|req:"+getCollectInfoReq);
            int total = collectDriverMapper.getCount(getCollectInfoReq);
            logger.info("getList|获取收藏司机数量|:"+total);
            if(getCollectInfoReq.isBeCount()){
                return new GetCollectListRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),total,0,null);
            }
            List<CollectDriver> entities = collectDriverMapper.list(getCollectInfoReq);
            Map<String,CollectedDriverModel>  models = new HashMap<String, CollectedDriverModel>();
            if(null != entities){
                logger.info("getList|获取收藏司机列表大小|:"+entities.size());
                for (CollectDriver entity: entities){
                    CollectedDriverModel model = transfer(entity);
                    models.put(String.valueOf(entity.getUserId()),model);
                }
            }
            return new GetCollectListRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),total,models.size(),models);

        }catch (Exception e){
            logger.error("getList|获取司机信息异常|:"+e.getMessage());
        }
        return new GetCollectListRes(Boolean.TRUE,CodeTypes.SystemError.getCodeStr(),CodeTypes.SystemError.getMsg(),0,0,null);
    }


    @Override
    public GetCollectListRes getList(GetCollectInfoReq getCollectInfoReq) {
        try{
            logger.info("getList|获取收藏司机列表|req:"+getCollectInfoReq);
            int total = collectDriverMapper.getCount(getCollectInfoReq);
            logger.info("getList|获取收藏司机数量|:"+total);
            Map<String,CollectedDriverModel>  collectDriversLists = new HashMap<String, CollectedDriverModel>();
            if(getCollectInfoReq.isBeCount()){
                return new GetCollectListRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),total,total,collectDriversLists);
            }
            //有分页的情况直接查库
            if(getCollectInfoReq.getPageNum()==null && getCollectInfoReq.getPageSize()==null){
                Gson gson = new Gson();
                //userid不为空时
                if(getCollectInfoReq.getUserId() != null){
                    //userid不为空，driverid不为空时
                    if(getCollectInfoReq.getDriverId()!=null){
                        String blackInfo = redisService.mapGet(COLLECTINFO_REDIS + getCollectInfoReq.getUserId(),getCollectInfoReq.getDriverId()+"");
                        if(blackInfo!=null){
                            CollectDriver collectDriver = gson.fromJson(blackInfo,CollectDriver.class);
                            CollectedDriverModel model = transfer(collectDriver);
                            collectDriversLists.put(String.valueOf(model.getUserId()),model);
                            return new GetCollectListRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),total,collectDriversLists.size(),collectDriversLists);
                        }
                    }else{
                        //userid不为空，driverid为空时
                        Map<String, String> collectDrivers =  redisService.mapGetAll(COLLECTINFO_REDIS + getCollectInfoReq.getUserId());
                        if(collectDrivers.size()>0){
                            for (String key : collectDrivers.keySet()) {
                                CollectDriver collectDriver = gson.fromJson(collectDrivers.get(key),CollectDriver.class);
                                CollectedDriverModel model = transfer(collectDriver);
                                collectDriversLists.put(String.valueOf(model.getDriverId()),model);
                            }
                            return new GetCollectListRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),total,collectDriversLists.size(),collectDriversLists);
                        }
                    }
                }else if(getCollectInfoReq.getDriverId() != null){
                    //driverid不为空，userid为空时
                    Map<String, String> collectDriversInfos =  redisService.mapGetAll(COLLECTINFO_REDIS + getCollectInfoReq.getDriverId());
                    if(collectDriversInfos.size()>0){
                        for (String key : collectDriversInfos.keySet()) {
                            CollectDriver collectDriver = gson.fromJson(collectDriversInfos.get(key),CollectDriver.class);
                            CollectedDriverModel model = transfer(collectDriver);
                            collectDriversLists.put(String.valueOf(model.getUserId()),model);
                        }
                        return new GetCollectListRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),total,collectDriversLists.size(),collectDriversLists);
                    }
                }
            }
            List<CollectDriver> entities = collectDriverMapper.list(getCollectInfoReq);
            if(null != entities){
                logger.info("getList|获取收藏司机列表大小|:"+entities.size());
                for (CollectDriver entity: entities){
                    CollectedDriverModel model = transfer(entity);
                    if(getCollectInfoReq.getDriverId()!=null){
                        collectDriversLists.put(String.valueOf(entity.getUserId()),model);
                    }else{
                        collectDriversLists.put(String.valueOf(entity.getDriverId()),model);
                    }
                    redisService.putObject2Map(COLLECTINFO_REDIS + entity.getUserId(),entity.getDriverId()+"",entity);
                    redisService.putObject2Map(COLLECTINFO_REDIS + entity.getDriverId(),entity.getUserId()+"",entity);
                    redisService.setTimeOut(COLLECTINFO_REDIS + entity.getUserId(),TIME_OUT);
                    redisService.setTimeOut(COLLECTINFO_REDIS + entity.getDriverId(),TIME_OUT);
                }
            }
            return new GetCollectListRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),total,collectDriversLists.size(),collectDriversLists);

        }catch (Exception e){
            logger.error("getList|获取司机信息异常|:"+e.getMessage());
        }
        return new GetCollectListRes(Boolean.TRUE,CodeTypes.SystemError.getCodeStr(),CodeTypes.SystemError.getMsg(),0,0,null);
    }

    @Override
    public void updateNote(UpdateNoteReq updateNoteReq) throws FeedException{
        logger.info("updateNote|receive update req:"+updateNoteReq);
        GetCollectInfoReq getCollectInfoReq = new GetCollectInfoReq();
        getCollectInfoReq.setUserId((long)updateNoteReq.getUserId());
        getCollectInfoReq.setDriverId(updateNoteReq.getDriverId());
        List<CollectDriver> list = collectDriverMapper.list(getCollectInfoReq);
        if (null == list || list.size() == 0){
            logger.error("updateNote|404|record not founded!|updateNoteReq:"+updateNoteReq);
            throw new FeedException(CodeTypes.NoRecord.getCode(),CodeTypes.NoRecord.getMsg());
        }
        CollectDriver collectDriver = new CollectDriver();
        collectDriver.setUserId(updateNoteReq.getUserId());
        collectDriver.setDriverId(updateNoteReq.getDriverId());
        collectDriver.setDriverNote(updateNoteReq.getDriverNote());
        collectDriver.setUpdateTime((long)DateUtil.getNumber(new Date()));
        try {
            int count = collectDriverMapper.updateNote(collectDriver);
            //修改数据库成功后，同时修改redis
            redisService.putObject2Map(COLLECTINFO_REDIS + collectDriver.getUserId(),collectDriver.getDriverId()+"",collectDriver);
            redisService.putObject2Map(COLLECTINFO_REDIS + collectDriver.getDriverId(),collectDriver.getUserId()+"",collectDriver);
            redisService.setTimeOut(COLLECTINFO_REDIS + collectDriver.getUserId(),TIME_OUT);
            redisService.setTimeOut(COLLECTINFO_REDIS + collectDriver.getDriverId(),TIME_OUT);
            logger.info("updateNote|success|"+collectDriver+"|count:"+count);
        } catch (Exception e) {
            logger.error("updateNote|500|update error|updateNoteReq:"+updateNoteReq);
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }
    }

    @Override
    public void updateServiceTimes(UpdateServiceTimesReq updateServiceTimesReq) throws FeedException {
        logger.info("updateNote|receive update req:"+updateServiceTimesReq);
        GetCollectInfoReq getCollectInfoReq = new GetCollectInfoReq();
        getCollectInfoReq.setUserId(updateServiceTimesReq.getUserId());
        getCollectInfoReq.setDriverId(updateServiceTimesReq.getDriverId());
        List<CollectDriver> list = collectDriverMapper.selectAllList(getCollectInfoReq);
        if (null == list || list.size() == 0){
            logger.info("updateServiceTimes|404|record not founded!|updateServiceTimesReq:"+updateServiceTimesReq);
            throw new FeedException(CodeTypes.NoRecord.getCode(),CodeTypes.NoRecord.getMsg());
        }
        CollectDriver collectDriver = list.get(0);
        collectDriver.setUserId(updateServiceTimesReq.getUserId());
        collectDriver.setDriverId(updateServiceTimesReq.getDriverId());
        collectDriver.setServiceTimes(updateServiceTimesReq.getServiceTimes());
        collectDriver.setUpdateTime((long)DateUtil.getNumber(new Date()));
        try {
            int count = collectDriverMapper.updateByPrimaryKey(collectDriver);
            //修改数据库成功后，同时修改redis
            redisService.putObject2Map(COLLECTINFO_REDIS + collectDriver.getUserId(),collectDriver.getDriverId()+"",collectDriver);
            redisService.putObject2Map(COLLECTINFO_REDIS + collectDriver.getDriverId(),collectDriver.getUserId()+"",collectDriver);
            redisService.setTimeOut(COLLECTINFO_REDIS + collectDriver.getUserId(),TIME_OUT);
            redisService.setTimeOut(COLLECTINFO_REDIS + collectDriver.getDriverId(),TIME_OUT);
            logger.info("updateServiceTimes|success|"+collectDriver+"|count:"+count);
        } catch (Exception e) {
            logger.error("updateServiceTimes|500|update error|updateServiceTimesReq:"+updateServiceTimesReq,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }
    }

    @Override
    public void withdraw(WithdrawCollectReq withdrawCollectReq) throws FeedException {
        logger.info("withdraw|req:"+withdrawCollectReq);
        List<CollectDriver> collectDrivers = new ArrayList<>();
        Long recordId = withdrawCollectReq.getCollectId();
        String userId = withdrawCollectReq.getUserId();
        if(recordId != null){
            CollectDriver collectDriver = collectDriverMapper.selectByPrimaryKey(recordId);
            collectDrivers.add(collectDriver);
        }else if(userId !=null){
            String driverId = withdrawCollectReq.getDriverId();
            GetCollectInfoReq info = new GetCollectInfoReq();
            info.setDriverId(Long.parseLong(driverId));
            info.setUserId(Long.parseLong(userId));
            List<CollectDriver> tmpCollects = collectDriverMapper.list(info);
            collectDrivers.addAll(tmpCollects);
        }
        if (0 == collectDrivers.size()){
            logger.info("withdraw|404|record not founded!|request info::"+withdrawCollectReq);
            throw new FeedException(CodeTypes.NoRecord.getCode(),CodeTypes.NoRecord.getMsg());
        }
        for(CollectDriver collDriver : collectDrivers){
            collDriver.setStatus((byte)2);
            try {
                collectDriverMapper.updateByPrimaryKey(collDriver);
                //修改数据库成功后，同时修改redis
                redisService.putObject2Map(COLLECTINFO_REDIS + collDriver.getUserId(),collDriver.getDriverId()+"",collDriver);
                redisService.putObject2Map(COLLECTINFO_REDIS + collDriver.getDriverId(),collDriver.getUserId()+"",collDriver);
                redisService.setTimeOut(COLLECTINFO_REDIS + collDriver.getUserId(),TIME_OUT);
                redisService.setTimeOut(COLLECTINFO_REDIS + collDriver.getDriverId(),TIME_OUT);
            } catch (Exception e) {
                logger.error("withdraw|500|update error|request info:"+withdrawCollectReq);
                throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
            }
        }
    }


    @Override
    public void add(AddCollectDriverReq addCollectDriverReq) throws FeedException {
        logger.info("add|req:"+addCollectDriverReq);
        Long driverId = addCollectDriverReq.getDriverId();
        Long userId = addCollectDriverReq.getUserId();
        //0.检查司机是否在乘客黑名单中
        GetBlackListReq getBlackListReq = new GetBlackListReq();
        getBlackListReq.setDriverId(driverId);
        getBlackListReq.setUserId(userId);
        GetBlackListByPageRes getBlackListByPageRes = blackListService.getBlackList(getBlackListReq);
        if(null != getBlackListByPageRes && getBlackListByPageRes.getCount() > 0){
            logger.info("add|司机已经被用户插入黑名单|"+getBlackListByPageRes);
            throw new FeedException(CodeTypes.Conflict.getCode(),"请删除黑名单后操作");
        }
        //1.根据driverId和userId查询服务次数
        int serviceCount = platformService.getDriverServiceCount(driverId,userId);
        logger.info("add|查询服务次数|driverId:"+driverId+"|userId:"+userId+"|serviceCount:"+serviceCount);
        //2.尝试更新服务次数，当更新ok表示数据库已经存在这条记录
        CollectDriver record = new CollectDriver();
        record.setUserId(userId);
        record.setDriverId(driverId);
        Date now = new Date();
        record.setUpdateTime((long)DateUtil.getNumber(now));
        record.setServiceTimes(serviceCount);
        int updateNum = collectDriverMapper.updateServiceTimes(record);
        if(updateNum > 0){
            logger.warn("add|已经存在收藏关系|updateNum:"+updateNum);
            throw new FeedException(CodeTypes.RepeatError.getCode(),"已存在收藏关系");
        }
        //3.新增收藏记录
        record.setCreateTime((long)DateUtil.getNumber(now));
        record.setStatus((byte)1);
        if(addCollectDriverReq.getDriverNote()!=null){
            record.setDriverNote(addCollectDriverReq.getDriverNote());
        }else{
            record.setDriverNote("");
        }
        //3.1获取司机城市
        try {
            String driverCity = getDriverCityByDriverId(addCollectDriverReq.getDriverId());
            if(driverCity==null){
                record.setDriverCity(new byte[1]);
            }else{
                record.setDriverCity(driverCity.getBytes("utf-8"));
            }
        } catch (Exception e) {
            logger.error("add|set driver city error|" + e.getMessage());
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }
        try {
             collectDriverMapper.insert(record);
            //collectDriverMangoDao.add(record);
        } catch (Exception e) {
            logger.error("add|500|add error|record:"+e.getMessage());
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }
        //4.收藏成功，发送通知
        platformService.sendSMS(driverId,NOTIFY_CONTENT);
        //数据插入数据库成功后插入到redis中
        redisService.putObject2Map(COLLECTINFO_REDIS + record.getUserId(),record.getDriverId()+"",record);
        redisService.putObject2Map(COLLECTINFO_REDIS + record.getDriverId(),record.getUserId()+"",record);
        redisService.setTimeOut(COLLECTINFO_REDIS + record.getUserId(),TIME_OUT);
        redisService.setTimeOut(COLLECTINFO_REDIS + record.getDriverId(),TIME_OUT);
    }

    private String getDriverCityByDriverId(long driverId) throws FeedException{
        Gson gson = new Gson();
        Map<String, Object> params =  new HashMap<String, Object>();
        params.put("driver_id",driverId);
        String driverCity ="";
        try {
            String requestUrl = ConfigManager.get(DictConfig.class).getDictMap().get("collectUrl");
            String res = HttpClientUtil.httpGetRequest(requestUrl,params);
            Map<String,Object> resultMap = gson.fromJson(res,Map.class);
            if("success".equals(resultMap.get("ret_msg"))){
                Map<String,String> resultValue = (Map<String, String>) resultMap.get("result");
                if(resultValue!=null){
                    driverCity = resultValue.get("city");
                }
            }
        } catch (URISyntaxException e) {
            logger.error("request the driver city interface error.driverId:"+driverId,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),"获取司机所在城市异常");
        }
        return driverCity;
    }

    private CollectedDriverModel transfer(CollectDriver entity){
        if (null == entity){
            return null;
        }
        CollectedDriverModel model = new CollectedDriverModel();
        model.setId(entity.getCollectDriverId());
        model.setUserId(entity.getUserId());
        model.setDriverId(entity.getDriverId());
        model.setDriverNote(entity.getDriverNote());
        if(null != entity.getDriverCity()){
            model.setDriverCity(new String(entity.getDriverCity()));
        }
        model.setServiceTimes(entity.getServiceTimes());
        model.setCreateTimeStamp(entity.getCreateTime());
        model.setUpdateTimeStamp(entity.getUpdateTime());
        return model;
    }

}




