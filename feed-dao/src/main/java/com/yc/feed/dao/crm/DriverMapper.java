package com.yc.feed.dao.crm;

import com.yc.feed.domain.entity.Driver;

import java.util.List;

public interface DriverMapper {
    int deleteByPrimaryKey(Integer driverId);

    int insert(Driver record);

    int insertSelective(Driver record);

    Driver selectByPrimaryKey(Long driverId);

    int updateByPrimaryKeySelective(Driver record);

    int updateByPrimaryKeyWithBLOBs(Driver record);

    int updateByPrimaryKey(Driver record);

    List<Long> getAllValidDriverIds(int shardTotal, int currentShard);
}