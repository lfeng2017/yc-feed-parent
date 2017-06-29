package com.yc.feed.service;

import java.util.Date;

/**
 * Created by yusong on 2016/10/20.
 * 与平台交互接口
 */
public interface PlatformService {

    /*
    *更新订单表
    */
    public boolean updateOrder(long orderId,boolean commentOk);

    /*
    *追加评价记录
    */
    public boolean appendTrack(long orderId, int userId, Date dateline);


    /*
    *好评时，调用司机端接口 发送短信通知
    */
    public boolean sendSMS(long driverId,String content);

    /*
    *用户收藏，根据司机id和用户id获得服务次数
    */
    public int getDriverServiceCount(long driverId,long userId);

}
