package com.yc.feed.service;

import com.yc.feed.api.http.req.GetBlackListReq;
import com.yc.feed.api.http.res.GetBlackListByPageRes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.req.AddBlackListReq;

import java.util.List;

/**
 * Created by yusong on 2016/10/29.
 * 黑名单服务
 */
public interface BlackListService {

    /*
    *获取黑名单列表
    */
    public GetBlackListByPageRes getBlackList(GetBlackListReq getBlackListReq);


    /*
    *新增黑名单
    */
    public boolean insertBlackList( AddBlackListReq domainReq) throws FeedException;

    /*
    *删除黑名单
    */
    public boolean deleteBlackList(Long id)throws FeedException;

    /*
    *删除黑名单by driverId useId
    */
    public boolean deleteBlackListByInfo(long driverId,long userId)throws FeedException;


}
