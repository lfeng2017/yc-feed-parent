package com.yc.feed.service;

import com.yc.feed.api.http.req.EvaluationRateReq;
import com.yc.feed.api.http.res.AddCommentRes;
import com.yc.feed.api.http.res.EvaluationRateRes;
import com.yc.feed.api.http.res.HisRateRes;
import com.yc.feed.api.http.res.MonthsEvaluationRes;
import com.yc.feed.domain.entity.OrderComment;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.OrderCommentInfo;
import com.yc.feed.domain.req.AddCommentReq;
import com.yc.feed.domain.req.ChangeCommentStatusReq;
import com.yc.feed.domain.req.GetCommentListReq;

import java.util.List;

/**
 * Created by yusong on 2016/10/13.
 * 用户评价HTTP服务
 */
public interface EvaluateHttpService {

    /*
    *添加用户评价
    */
    public AddCommentRes addUserComment(AddCommentReq addCommentReq) throws FeedException;

    /*
    *获取司机活动好评率
    */
    public EvaluationRateRes getDriverEvaluation(EvaluationRateReq evaluationRateReq);


    /*
    *根据订单号获取评价
    */
    public OrderCommentInfo getByOrderId(Long orderId) throws FeedException;


    /*
    *使订单失效
    */
    public void changeStatus(ChangeCommentStatusReq changeCommentStatusReq) throws FeedException;


    /*
    *获取订单评价列表
    */
    public List<OrderCommentInfo> getCommentList(GetCommentListReq req);
    
    /*
     *获取订单评价列表By IDs
     *orderIds:订单列表
     *tagFlag：0 默认返回所有标签 1只返回好评标签 -1只返回差评标签 
     */
     public List<OrderCommentInfo> getCommentListByIds(String orderIds, int tagFlag);

    /*
    *获取记录数量
    */
    public long getCount(GetCommentListReq req);


    /*
    *MonthsEvaluationRes
    * 星火项目按月份获取好评率和差评次数
    */
    public MonthsEvaluationRes getMonthsEvaluation(long startTime, long endTime, String driverIds);


    /*
    *change display_status
    */
    public void changeDisplayStatus(long orderId, int displayStatus, int operatorId) throws FeedException;

    /*
    * 根据司机id获得司机的平均分
    */
    public int getDriverAvgScore (Long driverId) throws FeedException;


}
