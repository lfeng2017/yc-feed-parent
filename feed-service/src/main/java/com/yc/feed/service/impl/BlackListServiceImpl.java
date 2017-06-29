package com.yc.feed.service.impl;

import com.google.gson.Gson;
import com.sun.net.httpserver.Authenticator;
import com.yc.feed.api.http.req.GetBlackListReq;
import com.yc.feed.api.http.req.GetCollectInfoReq;
import com.yc.feed.api.http.res.GetBlackListByPageRes;
import com.yc.feed.api.http.res.GetCollectListRes;
import com.yc.feed.api.util.DateUtil;
import com.yc.feed.dao.mapper.BlackListMapper;
import com.yc.feed.domain.entity.BlackList;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.req.AddBlackListReq;
import com.yc.feed.service.BlackListService;
import com.yc.feed.service.CollectInfoService;
import com.yc.feed.service.RedisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yusong on 2016/10/29.
 */
@Service
public class BlackListServiceImpl implements BlackListService {


    private final Logger logger = Logger.getLogger(BlackListServiceImpl.class);

    @Resource
    private BlackListMapper blackListMapper;
    @Autowired
    private CollectInfoService collectInfoService;
    @Autowired
    private RedisService<BlackList> redisService;

    private static final int TIME_OUT = 7*24*60*60;

    private static final String BLACKINFO_REDIS = "blackinfo:";


    @Override
    public GetBlackListByPageRes getBlackList(GetBlackListReq getBlackListReq) {
        //只查询记录总数
        if(getBlackListReq.isBeCount()){
            long count = blackListMapper.count(getBlackListReq);
            return new GetBlackListByPageRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),CodeTypes.Success.getMsg(),count,null);
        }
        List<BlackList> blackLists = new ArrayList<>();

        //有分页的情况直接查库
        if(getBlackListReq.getPageNum()==null && getBlackListReq.getPageSize()==null){
            Gson gson = new Gson();
            //userid不为空时
            if(getBlackListReq.getUserId() != null){
                //userid不为空，driverid不为空时
                if(getBlackListReq.getDriverId()!=null){
                    String blackInfo = redisService.mapGet(BLACKINFO_REDIS+getBlackListReq.getUserId(),getBlackListReq.getDriverId()+"");
                    if(blackInfo!=null){
                        BlackList blackList = gson.fromJson(blackInfo,BlackList.class);
                        if(getBlackListReq.getType()!=null && blackList.getType().equals(getBlackListReq.getType())
                                || getBlackListReq.getType()== null){
                            blackLists.add(blackList);
                            return new GetBlackListByPageRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),CodeTypes.Success.getMsg(),1,blackLists);
                        }
                    }
                }else{
                    //userid不为空，driverid为空时
                    Map<String, String> blackListInfos =  redisService.mapGetAll(BLACKINFO_REDIS+getBlackListReq.getUserId()+"");
                    if(blackListInfos.size()>0){
                        for (String key : blackListInfos.keySet()) {
                            BlackList blackList = gson.fromJson(blackListInfos.get(key),BlackList.class);
                            if(getBlackListReq.getType()!=null && blackList.getType().equals(getBlackListReq.getType())
                                    || getBlackListReq.getType()== null){
                                blackLists.add(blackList);
                            }
                        }
                        return new GetBlackListByPageRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),CodeTypes.Success.getMsg(),blackListInfos.size(),blackLists);
                    }
                }
            }else if(getBlackListReq.getDriverId() != null){
                //driverid不为空，userid为空时
                Map<String, String> blackListInfos =  redisService.mapGetAll(BLACKINFO_REDIS+getBlackListReq.getDriverId()+"");
                if(blackListInfos.size()>0){
                    for (String key : blackListInfos.keySet()) {
                        BlackList blackList = gson.fromJson(blackListInfos.get(key),BlackList.class);
                        if(getBlackListReq.getType()!=null && blackList.getType().equals(getBlackListReq.getType())
                                || getBlackListReq.getType()== null){
                            blackLists.add(blackList);
                        }
                    }
                    return new GetBlackListByPageRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),CodeTypes.Success.getMsg(),blackListInfos.size(),blackLists);
                }
            }
        }

        blackLists = blackListMapper.list(getBlackListReq);
        //记录插入到redis中
        for(BlackList blackList: blackLists){
            redisService.putObject2Map(BLACKINFO_REDIS + blackList.getUserId(),blackList.getDriverId()+"",blackList);
            redisService.putObject2Map(BLACKINFO_REDIS + blackList.getDriverId(),blackList.getUserId()+"",blackList);
            redisService.setTimeOut(BLACKINFO_REDIS + blackList.getUserId(),TIME_OUT);
            redisService.setTimeOut(BLACKINFO_REDIS + blackList.getDriverId(),TIME_OUT);
        }

        return new GetBlackListByPageRes(Boolean.TRUE, String.valueOf(CodeTypes.Success.getCode()),"黑名单查询成功",blackLists.size(),blackLists);
    }


    @Override
    public boolean insertBlackList(AddBlackListReq domainReq) throws FeedException {
        logger.info("|insertBlackList|添加黑名单请求|"+domainReq);
        //0.查询是否被收藏 (乘客拉黑司机时)
        if(1 == domainReq.getType()){
            GetCollectInfoReq getCollectInfoReq = new GetCollectInfoReq(domainReq.getDriverId(),domainReq.getUserId());
            getCollectInfoReq.setBeCount(true);
            GetCollectListRes getCollectListRes = collectInfoService.getList(getCollectInfoReq);
            if(null != getCollectListRes && getCollectListRes.getTotal() > 0){
                logger.error("|insertBlackList|已存在收藏关系|不能添加黑名单|");
                throw new FeedException(CodeTypes.Conflict4BlackList.getCode(),CodeTypes.Conflict4BlackList.getMsg());
            }
        }
        //1.如果司机已存在于乘客的黑名单列表，直接返回成功(private api逻辑)
        GetBlackListReq getBlackListReq = new GetBlackListReq();
        getBlackListReq.setDriverId(domainReq.getDriverId());
        getBlackListReq.setUserId(domainReq.getUserId());
        getBlackListReq.setType(domainReq.getType());
        long count = blackListMapper.count(getBlackListReq);
        if(count > 0){
            logger.warn("|insertBlackList|黑名单已经存在|直接返回成功|"+domainReq);
            return Boolean.TRUE;
        }

        //2.插入新纪录
        BlackList record = new BlackList();
        record.setUserId(domainReq.getUserId());
        record.setDriverId(domainReq.getDriverId());
        record.setType(domainReq.getType());
        record.setCreateTime((long)DateUtil.getNumber(new Date()));
        record.setServiceOrderId(domainReq.getServiceOrderId());
        logger.info("insertBlackList|新增黑名单|"+record);
        try {
            blackListMapper.insert(record);
            logger.info("insertBlackList|新增黑名单成功入mysql成功|");
        } catch (Exception e) {
            logger.error("insertBlackList|新增黑名单错误|userId:"+domainReq.getUserId(),e);
            throw new FeedException(CodeTypes.SystemError.getCode(),"新增黑名单错误");
        }

        //更新redis
        redisService.putObject2Map(BLACKINFO_REDIS + record.getUserId(),record.getDriverId()+"",record);
        redisService.putObject2Map(BLACKINFO_REDIS + record.getDriverId(),record.getUserId()+"",record);
        redisService.setTimeOut(BLACKINFO_REDIS + record.getUserId(),TIME_OUT);
        redisService.setTimeOut(BLACKINFO_REDIS + record.getDriverId(),TIME_OUT);
        return Boolean.TRUE;
    }

    @Override
    public boolean deleteBlackList(Long id) throws FeedException {
        logger.info("deleteBlackList|删除黑名单|id:"+id);
        try {
            //删除redis中的数据
            BlackList blackList = blackListMapper.selectByPrimaryKey(id);
            if(blackList!=null){
                Long count = redisService.delMapKey(BLACKINFO_REDIS +blackList.getUserId(),blackList.getDriverId()+"");
                Long count1 = redisService.delMapKey(BLACKINFO_REDIS +blackList.getDriverId(),blackList.getUserId()+"");
                blackListMapper.deleteByPrimaryKey(id);
                if(count!=null && count1!=null){
                    logger.info("deleteBlackList|从redis删除黑名单成功|id:"+id);
                }else{
                    logger.info("deleteBlackList|从redis删除黑名单失败|id:"+id);
                }
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            logger.error("deleteBlackList|删除黑名单错误|id:"+id,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),"删除黑名单错误");
        }
    }

    @Override
    public boolean deleteBlackListByInfo(long driverId, long userId) throws FeedException {
        logger.info("deleteBlackListByInfo|删除黑名单|driverId,userId:"+driverId + ":" + userId);
        try {
            BlackList record = new BlackList();
            record.setDriverId(driverId);
            record.setUserId(userId);
            int count = blackListMapper.deleteByInfo(record);
            Long redisCount = redisService.delMapKey(BLACKINFO_REDIS+driverId,userId+"");
            Long redisCount1 = redisService.delMapKey(BLACKINFO_REDIS+userId,driverId+"");
            if(redisCount!=null && redisCount1!=null){
                logger.info("deleteBlackListByInfo|从redis删除黑名单成功|driverId:"+driverId+"|userId"+userId);
            }else{
                logger.info("deleteBlackListByInfo|从redis删除黑名单失败|driverId:"+driverId+"|userId"+userId);
            }
            if(count >0){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            logger.error("deleteBlackListByInfo|删除黑名单|driverId,userId:"+driverId + ":" + userId);
            throw new FeedException(CodeTypes.SystemError.getCode(),"删除黑名单");
        }
    }
}
