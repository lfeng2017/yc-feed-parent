package com.yc.feed.dao.mapper;

import com.yc.feed.domain.entity.OrderCommentTag;
import com.yc.feed.domain.model.CommentTagResults;

import java.util.List;

public interface OrderCommentTagMapper {
    int deleteByPrimaryKey(Integer commentTagDetailId);

    int insert(OrderCommentTag record);

    int insertSelective(OrderCommentTag record);

    OrderCommentTag selectByPrimaryKey(Integer commentTagDetailId);

    int updateByPrimaryKeySelective(OrderCommentTag record);

    int updateByPrimaryKey(OrderCommentTag record);

    List<OrderCommentTag> getByOrderId(Long orderId);

    List<CommentTagResults> getByOrderIds(String[] orderIds);
}