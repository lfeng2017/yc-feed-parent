package com.yc.feed.service;

import com.yc.feed.api.http.req.AddCommentTagReq;
import com.yc.feed.api.http.req.GetTagsReq;
import com.yc.feed.domain.model.CommentTagModel;
import com.yc.feed.domain.enums.ValidateTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.CommentTagResults;
import com.yc.feed.domain.req.UpdateTagsReq;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yusong on 2016/10/8.
 * 用户评价标签服务
 */
public interface TagService {



    /*
    *获取最终评价
    */
    public ValidateTypes getFinalValuate(List<Integer> commentTagId);

    /*
    *插入评价标签
    */
    public void insertOrderTag(long orderId,int driverID,int tagId) throws FeedException;


    /*
    *获取所有标签
    */
    public List<CommentTagModel> getAllCommentTag();


     /*
    *获取所有负面标签
    */
     public List<CommentTagModel> getAllNegativeCommentTag();

    /*
    *根据订单号获取标签列表
    */
    public List<CommentTagModel> getOrderTags(Long orderId);


    /*
    *根据ID获取
     */
    public CommentTagModel getTag(int tagId);


    /*
    *根据检索字符串获取
    */
    public List<CommentTagModel> getByQueryString(GetTagsReq req) throws FeedException;

    /*
    *更新评价标签
    */
    public void update(UpdateTagsReq updateReq) throws FeedException;

    /*
     *插入标签
     */
    public void insertTag(AddCommentTagReq insert) throws FeedException;

    /*
     *  根据司机id，得到评价标签最频繁的标签
     */
    public HashMap<String,String> getTop3Tags(String driverIds) throws Exception;

    /*
    *  根据司机id，得到评价标签最频繁的标签
    */
    public void runDriverTop3Tags(int shardTotal,int currentShard);


}
