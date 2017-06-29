package com.yc.feed.lock;

import java.util.concurrent.TimeUnit;

/**
 * Created by yusong on 2016/11/21.
 * 锁接口
 */
public interface Lock {

    boolean tryLock();


    boolean tryLock( int seconds);


    void unlock();

}
