package com.yc.feed.dao.mapper;

import com.yc.feed.api.http.req.GetTagsReq;
import com.yc.feed.domain.entity.CommentTag;

import java.util.List;

public interface CommentTagMapper {
    int deleteByPrimaryKey(Integer commentTagId);

    int insert(CommentTag record);

    int insertSelective(CommentTag record);

    CommentTag selectByPrimaryKey(Integer commentTagId);

    int updateByPrimaryKeySelective(CommentTag record);

    int updateByPrimaryKey(CommentTag record);

    /*
    *所有标签条件查询
    */
    List<CommentTag> list(CommentTag commentTag);


    /*
    *条件查询
    */
    List<CommentTag> listByQueryString(GetTagsReq req);

    /*
    *负面标签定制查询
    */
    List<CommentTag> listNegative();
}