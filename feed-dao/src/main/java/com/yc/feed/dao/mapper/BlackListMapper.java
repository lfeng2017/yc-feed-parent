package com.yc.feed.dao.mapper;

import com.yc.feed.api.http.req.GetBlackListReq;
import com.yc.feed.domain.entity.BlackList;

import java.util.List;

public interface BlackListMapper {
    int deleteByPrimaryKey(Long blackListId);
    int deleteByInfo(BlackList record);

    int insert(BlackList record);

    int insertSelective(BlackList record);

    BlackList selectByPrimaryKey(Long blackListId);

    int updateByPrimaryKeySelective(BlackList record);

    int updateByPrimaryKey(BlackList record);

    /*
    *获取列表
    */
    List<BlackList> list(GetBlackListReq getBlackListReq);

    /*
    *获取数目
    */
    long count(GetBlackListReq getBlackListReq);

}