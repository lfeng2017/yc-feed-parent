package com.yc.feed.service;

import com.yc.feed.lock.Lock;

/**
 * Created by yusong on 2016/11/21.
 * 锁服务
 */
public interface LockService {

    /*
    *获取锁
    */
    public Lock getLock(String lockId);
    /*
     * 释放map的key
     */
    public void removeLock(String lockId);
}
