package com.yc.feed.api.util;

import com.yc.feed.api.constant.RedisKey;

/**
 * Created by yusong on 2016/10/10.
 * 生成、获取 Redis Key 工具类
 */
public class RedisKeyUtil {

    /*
    *获取司机活动好评率Redis Key
    */
    public static String getFeedbackRateKey(long activityId,long driverId){
        StringBuffer sb = new StringBuffer();
        sb.append(RedisKey.ACTIVITY_DRIVER_INFO_KEY_PREFIX).append(activityId).append(RedisKey.JOIN_KEY)
                .append(driverId);
        return sb.toString();
    }

    /*
    *获取司机历史好评率
    */
    public static String getHisRateKey(long driverId ){
        return RedisKey.DRIVER_HIS_EVAL_PREFIX + String.valueOf(driverId);
    }



}
