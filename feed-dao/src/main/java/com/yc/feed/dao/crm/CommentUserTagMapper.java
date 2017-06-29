package com.yc.feed.dao.crm;

import com.yc.feed.domain.entity.CommentTag;
import com.yc.feed.domain.entity.CommentUserTag;

import java.util.List;

public interface CommentUserTagMapper {
    int deleteByPrimaryKey(Integer commentUserTagId);

    int insert(CommentUserTag record);

    int insertSelective(CommentUserTag record);

    CommentUserTag selectByPrimaryKey(Integer commentUserTagId);

    int updateByPrimaryKeySelective(CommentUserTag record);

    int updateByPrimaryKey(CommentUserTag record);

    /*
    *所有标签条件查询
    */
    List<CommentUserTag> list(CommentUserTag commentUserTag);
}