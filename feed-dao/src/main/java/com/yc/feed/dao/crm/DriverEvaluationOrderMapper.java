package com.yc.feed.dao.crm;

import com.yc.feed.domain.entity.DriverEvaluationOrder;

public interface DriverEvaluationOrderMapper {
    int deleteByPrimaryKey(Integer driverEvaluationOrderId);

    int insert(DriverEvaluationOrder record);

    int insertSelective(DriverEvaluationOrder record);

    DriverEvaluationOrder selectByPrimaryKey(Integer driverEvaluationOrderId);

    int updateByPrimaryKeySelective(DriverEvaluationOrder record);

    int updateByPrimaryKey(DriverEvaluationOrder record);

    DriverEvaluationOrder selectByDriverId(Long driverId);

    int updateOrder(DriverEvaluationOrder record);
}