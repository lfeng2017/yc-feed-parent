package com.yc.feed.dao.mapper;


import com.yc.feed.domain.entity.OrderComment;
import com.yc.feed.domain.model.DriverRecentOrderComment;
import com.yc.feed.domain.req.GetCommentListReq;

import java.util.List;

public interface OrderCommentMapper {

    int deleteByPrimaryKey(Integer orderCommentId);

    int insert(OrderComment record);

    int insertSelective(OrderComment record);

    OrderComment selectByPrimaryKey(Integer orderCommentId);

    OrderComment selectByOrderId(Long orderId);

    int updateByPrimaryKeySelective(OrderComment record);

    int updateByPrimaryKey(OrderComment record);

    int updateStatus(OrderComment record);

    int changeDisplayStatus(OrderComment record);

    List<OrderComment> list(GetCommentListReq getCommentListReq);

    List<OrderComment> listByEndTime(GetCommentListReq getCommentListReq);

    long count(GetCommentListReq getCommentListReq);

    float getDriverAvgScore(Long driverId);

    /*
    *根据driverid获取50个订单有效评价
    */
    List<OrderComment> getLast50(Long driverId);

    List<DriverRecentOrderComment> getLast50Comment();
}