package com.yc.feed.service.impl;

import com.yc.feed.lock.Lock;
import com.yc.feed.lock.RedisLock;
import com.yc.feed.service.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yusong on 2016/11/21.
 * 锁服务实现
 */
@Service
public class LockServiceImpl implements LockService {

    @Autowired
    private JedisCluster jedisCluster;
    //锁列表
    private final static Map<String,Lock> lockMap = new HashMap<String, Lock>();

    @Override
    public Lock getLock(String lockId) {
        Lock lock = lockMap.get(lockId);
        if (null == lock){
            lock = new RedisLock(jedisCluster,lockId);
            lockMap.put(lockId,lock);
        }
        return lock;
    }

    @Override
    public void removeLock(String lockId) {
        lockMap.remove(lockId);
    }
}
