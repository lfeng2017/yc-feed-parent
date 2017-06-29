package com.yc.feed.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yc.feed.service.RedisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * Created by yusong on 2016/3/20.
 */
@Service
public class RedisServiceImpl<E> implements RedisService<E> {


    private final Logger logger = Logger.getLogger(RedisServiceImpl.class);

    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public void put(String key, Object value) {
        Gson gson = new Gson();
        try {
            jedisCluster.set(key, gson.toJson(value));
        } catch (Exception e) {
            logger.error("RedisService|put|redis写入错误|",e);
        }
    }

    @Override
    public void put(String key, Object value, int seconds) {
        Gson gson = new Gson();
        try {
            jedisCluster.setex(key,seconds, gson.toJson(value));
        } catch (Exception e) {
            logger.error("RedisService|put|redis写入错误|",e);
        }
    }

    @Override
    public String getStr(String key) {
        String res = null;
        try {
            res = jedisCluster.get(key);
        } catch (Exception e) {
            logger.error("RedisService|getStr|redis读取错误|",e);
        }
        return res;
    }

    @Override
    public void put2Map(String redisKey, String mapKey, String value) {
        try {
            jedisCluster.hset(redisKey,mapKey,value);
        } catch (Exception e) {
            logger.error("RedisService|put2Map|redis写入MAP错误|",e);
        }
    }

    @Override
    public void putObject2Map(String redisKey, String mapKey, E value) {
        Gson gson = new Gson();
        put2Map(redisKey,mapKey,gson.toJson(value));
    }

    @Override
    public String mapGet(String redisKey, String mapKey) {
        String res = null;
        try {
            res = jedisCluster.hget(redisKey,mapKey);
        } catch (Exception e) {
            logger.error("RedisService|mapGet|redis读取MAP错误|",e);
        }
        return res;
    }


    @Override
    public Map<String, String> mapGetAll(String redisKey) {
        Map<String, String> res = null;
        try {
            res = jedisCluster.hgetAll(redisKey);
        } catch (Exception e) {
            logger.error("RedisService|mapGetAll|redis读取MAP错误|",e);
        }
        return res;
    }

    @Override
    public Long delMapKey(String redisKey, String mapKey) {
        Long count=null;
        try {
            count = jedisCluster.hdel(redisKey,mapKey);
        } catch (Exception e) {
            logger.error("RedisService|delMapKey|redis删除数据错误|",e);
        }
        return count;
    }


    @Override
    public List<E> getList(String redisKey) {
        String jsonStr = null;
        try {
            jsonStr = jedisCluster.get(redisKey);
        } catch (Exception e) {
            logger.error("RedisService|getList|redis读取错误|",e);
            return null;
        }
        Gson gson = new Gson();
        List<E> list = null ;
        try {
            list = gson.fromJson(jsonStr, new TypeToken<List<E>>() {
                                                            }.getType());
        } catch (JsonSyntaxException e) {
            logger.error("RedisService|getList|redis解析错误|jsonStr:"+jsonStr,e);
        }
        return list;
    }

    @Override
    public boolean saveList(String redisKey, List<E> list) {
        Gson gson = new Gson();
        try {
            jedisCluster.set(redisKey,gson.toJson(list));
        } catch (Exception e) {
            logger.error("RedisService|saveList|redis写入错误|",e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public boolean setTimeOut(String redisKey, int seconds) {
        try {
            jedisCluster.expire(redisKey,seconds);
        } catch (Exception e) {
            logger.error("RedisService|setTimeOut|redis设置过期时间报错|",e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}







