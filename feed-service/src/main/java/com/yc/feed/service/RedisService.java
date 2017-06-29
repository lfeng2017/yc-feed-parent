package com.yc.feed.service;

import java.util.List;
import java.util.Map;

/**
 * Created by yusong on 15-12-15.
 * 说明：redis服务
 */
public interface RedisService<E> {



    /*
    *存储对象
    */
    public void put(String key, Object value);

    /*
    *存储对象
    */
    public void put(String key, Object value,int seconds);
    /*
    *获取String
    */
    public String getStr(String key);


    /*
    *存储Map对象
    */
    public void put2Map(String redisKey, String mapKey, String value);


    /*
    *存储Map对象
    */
    public void putObject2Map(String redisKey, String mapKey, E value);

    /*
    *获取Map中的value
    */
    public String mapGet(String redisKey, String mapKey);

    /*
    *获取Map中的所有的值
    */
    public Map<String,String> mapGetAll(String redisKey);

    /*
    *获取Map中数据
    */
    public Long delMapKey(String redisKey,String mapKey);


    //getList 和 saveList把List当一个对象操作，进行存取
    /*
    *获取列表对象
    */
    public List<E> getList(String redisKey);

    /*
    *存储列表
    */
    public boolean saveList(String redisKey, List<E> list);

    /*
    * 设置key的过期时间
    */
    public boolean setTimeOut(String redisKey, int seconds);
}
