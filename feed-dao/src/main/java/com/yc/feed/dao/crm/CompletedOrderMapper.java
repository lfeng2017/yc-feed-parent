package com.yc.feed.dao.crm;

import com.yc.feed.domain.entity.CompletedOrder;

import java.util.List;

public interface CompletedOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CompletedOrder record);

    int insertSelective(CompletedOrder record);

    CompletedOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CompletedOrder record);

    int updateByPrimaryKey(CompletedOrder record);

    int updateStatus(CompletedOrder record);

    List<CompletedOrder> list(long minEndTime);


}