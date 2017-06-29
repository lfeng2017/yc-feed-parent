package com.yc.feed.lock;

import org.apache.log4j.Logger;
import redis.clients.jedis.JedisCluster;

/**
 * Created by yusong on 2016/11/21.
 * 基于Redis的分布式锁
 */
public class RedisLock implements Lock{
    private final Logger logger = Logger.getLogger(RedisLock.class);

    //redis服务
    private JedisCluster jedisCluster;
    //锁的唯一标识
    private String lockId;

    @Override
    public boolean tryLock() {
        try {
            Long res = jedisCluster.setnx(lockId,"Locked");
            return res == 1;
        } catch (Exception e) {
            logger.error("tryLock|获取redis锁异常|lockId:"+lockId,e);
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean tryLock(int seconds) {
        if(tryLock()){
            jedisCluster.setex(lockId,seconds,"Locked");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public void unlock() {
        try {
            jedisCluster.del(lockId);
        } catch (Exception e) {
            logger.error("tryLock|释放redis锁异常|lockId:"+lockId,e);
        }
    }

    public RedisLock(JedisCluster jedisCluster, String lockId) {
        this.jedisCluster = jedisCluster;
        this.lockId = lockId;
    }

    public String getLockId() {
        return lockId;
    }
}
