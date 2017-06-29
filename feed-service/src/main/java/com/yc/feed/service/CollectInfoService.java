package com.yc.feed.service;

import com.yc.feed.api.http.req.GetCollectInfoReq;
import com.yc.feed.api.http.res.GetCollectCountRes;
import com.yc.feed.api.http.res.GetCollectListRes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.CollectedDriverModel;
import com.yc.feed.domain.req.AddCollectDriverReq;
import com.yc.feed.domain.req.UpdateNoteReq;
import com.yc.feed.domain.req.UpdateServiceTimesReq;
import com.yc.feed.domain.req.WithdrawCollectReq;

import java.util.List;

/**
 * Created by ke on 16-11-1.
 * 评价与收藏服务
 */
public interface CollectInfoService {

    /*
    *根据司机ID 获取用户数目
    */
    public GetCollectCountRes getUserCountByDriverId(long driverId);

    /*
    *获取列表
    */
    public GetCollectListRes getList(GetCollectInfoReq getCollectInfoReq);

    /*
    *根据driverid获取收藏列表
    */
    public GetCollectListRes getListByDriverId(GetCollectInfoReq getCollectInfoReq);

    /*
    *更新备注
    */
    public void updateNote(UpdateNoteReq updateNoteReq) throws FeedException;

    /*
    *更新服务次数
    */
    public void updateServiceTimes(UpdateServiceTimesReq updateServiceTimesReq)throws FeedException;

    /*
    *撤销收藏
    */
    public void withdraw(WithdrawCollectReq withdrawCollectReq) throws FeedException;

    /*
    *新增收藏
    */
    public void add(AddCollectDriverReq addCollectDriverReq)  throws FeedException;


}




