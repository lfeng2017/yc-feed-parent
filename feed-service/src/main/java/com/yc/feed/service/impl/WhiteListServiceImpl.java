package com.yc.feed.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.le.config.client.ConfigManager;
import com.yc.feed.api.util.HttpClientUtil;
import com.yc.feed.domain.config.DictConfig;
import com.yc.feed.service.RedisService;
import com.yc.feed.service.WhiteListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yusong on 2016/12/7.
 * 白名单服务实现
 */
@Service
public class WhiteListServiceImpl implements WhiteListService {

    private final Logger logger = Logger.getLogger(WhiteListServiceImpl.class);

    @Autowired
    private RedisService redisService;
    //Redis 缓存键
    private final static String BLACK_LIST_CACHE_KEY  = "feed:driver:black;list";
    //查询参数
    private final static Map<String, Object> PARA_MAP =  new HashMap<String, Object>();

    static {
        PARA_MAP.put("brand_car_id","86");
    }


    @Override
    public boolean beWhiteList(Long driverId) {
        logger.info("beWhiteList|查询白名单请求|driverId:"+driverId);
        List<Long> whiteList = getCachedList();
        boolean res = false;
        if(null != whiteList){
            res = whiteList.contains(driverId);
            logger.info("beWhiteList|根据缓存查询|driverId:"+driverId+"|"+res);
            return res;
        }
        whiteList = getRemoteList();
        if(whiteList == null || whiteList.size() == 0){
            logger.warn("whiteList|未查询到司机端白名单|");
            return res;
        }
        int expireTime = Integer.parseInt(ConfigManager.get(DictConfig.class).getDictMap().get("driverWhiteCacheTime"));
        redisService.put(BLACK_LIST_CACHE_KEY,whiteList,expireTime);
        res = whiteList.contains(driverId);
        logger.info("beWhiteList|根据接口查询|driverId:"+driverId+"|"+res);
        return res;
    }

    /*
    *获取远程司机端列表
    */
    private List<Long> getRemoteList(){
        String resJson = null;
        Gson gson = new Gson();
        List<Long> whiteList = new ArrayList<Long>();
        String queryWhiteListUrl = ConfigManager.get(DictConfig.class).getDictMap().get("driverWhiteListUrl");
        logger.info("getRemoteList|调用司机端查询白名单|url:"+queryWhiteListUrl+"|para:"+gson.toJson(PARA_MAP));
        try {
            resJson = HttpClientUtil.httpGetRequest(queryWhiteListUrl,PARA_MAP);
            logger.info("getRemoteList|调用司机端查询白名单响应|res:"+resJson);
        } catch (Exception e) {
            logger.error("getRemoteList|查询白名单接口出错|",e);
            return whiteList;
        }
        if(StringUtils.isEmpty(resJson)){
            logger.warn("getRemoteList|返回数据为空|");
            return whiteList;
        }
        WhiteRes whiteRes = null;
        try {
            whiteRes = gson.fromJson(resJson,WhiteRes.class);
            logger.info("getRemoteList|解析ok|"+whiteRes);
        } catch (Exception e) {
            logger.error("getRemoteList|解析结果错误|"+resJson,e);
            return whiteList;
        }
        if(null != whiteRes){
            List<DriverInfo> result = whiteRes.getResult();
            logger.info("getRemoteList|查询司机列表数目|"+(result == null ? 0:result.size()));
            if (null != result || result.size() > 0){
                for(DriverInfo driverInfo : result){
                    whiteList.add(driverInfo.getDriver_id());
                }
            }
        }
        return whiteList;
    }

    /*
    *获取缓存中的白名单
    */
    private List<Long> getCachedList(){
        String jsonStr = redisService.getStr(BLACK_LIST_CACHE_KEY);
        if(StringUtils.isEmpty(jsonStr)){
            logger.warn("getCachedList|缓存中无数据|");
            return null;
        }
        Gson gson = new Gson();
        List<Long> driverIds = null ;
        try {
            driverIds = gson.fromJson(jsonStr, new TypeToken<List<Long>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            logger.warn("getCachedList|解析缓存数据错误|"+jsonStr,e);
        }
        logger.info("getCachedList|缓存中白名单数量|"+(driverIds == null ? 0:driverIds.size()));
        return driverIds;
    }

    /*
    *司机端返回接口模型
    */
    private class WhiteRes{
        //返回码
        private int ret_code;
        //返回描述
        private String ret_msg;
        //列表
        private List<DriverInfo> result;

        @Override
        public String toString() {
            return "WhiteRes{" +
                    "ret_code=" + ret_code +
                    ", ret_msg='" + ret_msg + '\'' +
                    ", result=" + result +
                    '}';
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public String getRet_msg() {
            return ret_msg;
        }

        public void setRet_msg(String ret_msg) {
            this.ret_msg = ret_msg;
        }

        public List<DriverInfo> getResult() {
            return result;
        }

        public void setResult(List<DriverInfo> result) {
            this.result = result;
        }
    }

    private class DriverInfo{
        //车类型
        private String brand_car_id;
        //司机ID
        private Long driver_id;

        @Override
        public String toString() {
            return "DriverInfo{" +
                    "brand_car_id='" + brand_car_id + '\'' +
                    ", driver_id=" + driver_id +
                    '}';
        }

        public String getBrand_car_id() {
            return brand_car_id;
        }

        public void setBrand_car_id(String brand_car_id) {
            this.brand_car_id = brand_car_id;
        }

        public Long getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(Long driver_id) {
            this.driver_id = driver_id;
        }
    }


}
