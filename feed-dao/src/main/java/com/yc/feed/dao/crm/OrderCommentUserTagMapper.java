package com.yc.feed.dao.crm;

import com.yc.feed.domain.entity.OrderCommentUserTag;

import java.util.List;

/*
*司机评价乘客标签
*/
public interface OrderCommentUserTagMapper {
    int deleteByPrimaryKey(Integer orderCommentUserTagsId);

    int insert(OrderCommentUserTag record);

    int insertSelective(OrderCommentUserTag record);

    OrderCommentUserTag selectByPrimaryKey(Integer orderCommentUserTagsId);

    int updateByPrimaryKeySelective(OrderCommentUserTag record);

    int updateByPrimaryKey(OrderCommentUserTag record);

    List<OrderCommentUserTag> getByOrderId(Long orderId);

}