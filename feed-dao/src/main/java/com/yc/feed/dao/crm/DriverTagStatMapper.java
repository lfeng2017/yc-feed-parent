package com.yc.feed.dao.crm;

import com.yc.feed.domain.entity.DriverTagStat;

import java.util.List;

public interface DriverTagStatMapper {
    int deleteByPrimaryKey(Long driverCommentTagStatisticsId);

    int deleteByDriverId(Long driverId);

    int insert(DriverTagStat record);

    int insertSelective(DriverTagStat record);

    DriverTagStat selectByPrimaryKey(Long driverCommentTagStatisticsId);

    int updateByPrimaryKeySelective(DriverTagStat record);

    int updateByPrimaryKey(DriverTagStat record);

    List<DriverTagStat> selectByDriverId(Long driverId);

    /*
    *对累计数目进行原子+
    */
    int addCount(DriverTagStat record);



}