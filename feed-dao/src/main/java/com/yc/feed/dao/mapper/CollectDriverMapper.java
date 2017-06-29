package com.yc.feed.dao.mapper;

import com.yc.feed.api.http.req.GetCollectInfoReq;
import com.yc.feed.domain.entity.CollectDriver;

import java.util.List;

public interface CollectDriverMapper {
    int deleteByPrimaryKey(Long collectDriverId);

    int insert(CollectDriver record);

    int insertSelective(CollectDriver record);

    CollectDriver selectByPrimaryKey(Long collectDriverId);

    int updateByPrimaryKeySelective(CollectDriver record);

    int updateByPrimaryKeyWithBLOBs(CollectDriver record);

    int updateByPrimaryKey(CollectDriver record);

    int getCount(GetCollectInfoReq getCollectInfoReq);

    List<CollectDriver> list(GetCollectInfoReq getCollectInfoReq);

    List<CollectDriver> selectAllList(GetCollectInfoReq getCollectInfoReq);

    int updateNote(CollectDriver record);

    int updateServiceTimes(CollectDriver record);
}