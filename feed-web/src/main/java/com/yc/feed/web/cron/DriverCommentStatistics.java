/*
package com.yc.feed.web.cron;
import com.yc.feed.api.util.DateUtil;
import com.yc.feed.dao.crm.DriverEvaluationOrderMapper;
import com.yc.feed.dao.crm.DriverTagStatMapper;
import com.yc.feed.dao.mapper.OrderCommentMapper;
import com.yc.feed.dao.mapper.OrderCommentTagMapper;
import com.yc.feed.domain.entity.*;
import com.yc.feed.domain.model.CommentTagModel;
import com.yc.feed.domain.model.CommentTagResults;
import com.yc.feed.domain.model.DriverRecentOrderComment;
import com.yc.feed.service.TagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;


*/
/**
 * Created by lfeng on 2017/2/13.
 *
 * 重构php代码：1.统计隔天的所有评价的各个标签的数量
 *              2.更新司机的最近50单数据
 *//*

@Component
public class DriverCommentStatistics {
    private final Logger logger = Logger.getLogger(DriverCommentStatistics.class);

    @Autowired
    private TagService tagService;
    @Resource
    private OrderCommentMapper orderCommentMapper;
    @Resource
    private OrderCommentTagMapper orderCommentTagMapper;
    @Resource
    private DriverTagStatMapper driverTagStatMapper;
    @Resource
    private DriverEvaluationOrderMapper driverEvaluationOrderMapper;

    */
/*
    *  该功能在用户添加用户评价时已经实现实时更新
    *  以下代码还没上线
    * *//*

    public void updateDriverCommentTagStatistics(){
        List<CommentTagModel> allTags = tagService.getAllCommentTag();
        //获取所有司机的最近50个的订单评价记录
        List<DriverRecentOrderComment> recentOrderComments = orderCommentMapper.getLast50Comment();
        logger.info("updateDriverCommentTagStatistics|获得司机信息|Count:"+ recentOrderComments.size());

        for(DriverRecentOrderComment orderComment : recentOrderComments){
            if(orderComment.getOrders() != null){
                String[] orders = orderComment.getOrders().split(",");
                Date now = new Date();
                long startTime =  System.currentTimeMillis();
                //获取当前司机50单的所有评价统计数据，并将当前司机下以前的数据清除
                List<CommentTagResults> tagStatResults = orderCommentTagMapper.getByOrderIds(orders);
                int deleteFlag = driverTagStatMapper.deleteByDriverId(orderComment.getDriverId());
                logger.info("updateDriverCommentTagStatistics|删除司机"+orderComment.getDriverId()+"|Result:"+ deleteFlag);
                for(CommentTagResults tagResult :tagStatResults ){
                    CommentTagModel commentTagModel = tagService.getTag(tagResult.getCommentTagId());
                    logger.info("========================:"+ commentTagModel);
                    DriverTagStat driverTagStat = new DriverTagStat();
                    driverTagStat.setDriverId(orderComment.getDriverId());
                    driverTagStat.setCreateTime(DateUtil.getNumber(now));
                    driverTagStat.setCommentTagId(tagResult.getCommentTagId());
                    driverTagStat.setTagText(commentTagModel.getTagText());
                    driverTagStat.setCount(tagResult.getCount());
                    driverTagStat.setType(commentTagModel.getType());
                    driverTagStatMapper.insert(driverTagStat);
                }

                //缓存司机最新订单
                DriverEvaluationOrder driverEvaluationOrder = driverEvaluationOrderMapper.selectByDriverId(orderComment.getDriverId());
                logger.info("========================:"+ driverEvaluationOrder);
                if(driverEvaluationOrder != null){
                    driverEvaluationOrder.setRecentOrderString(orderComment.getOrders());
                    driverEvaluationOrderMapper.updateOrder(driverEvaluationOrder);
                }else{
                    DriverEvaluationOrder record = new DriverEvaluationOrder();
                    record.setDriverId(orderComment.getDriverId());
                    record.setRecentOrderString(orderComment.getOrders());
                    record.setCreateTime(DateUtil.getNumber(now));
                    record.setUpdateTime(DateUtil.getNumber(now));
                    driverEvaluationOrderMapper.insert(record);
                }
                logger.info("updateDriverCommentTagStatistics|更新司机ID"+orderComment.getDriverId()+"响应:"+(System.currentTimeMillis()-startTime)+"ms");
            }
        }
        logger.info("==================updateDriverCommentTagStatistics|任务结束");
    }*//*

}






*/
