package com.yc.feed.service;

import com.yc.feed.api.http.req.WebAddJudgementTagReq;
import com.yc.feed.api.http.res.GetJudgementListRes;
import com.yc.feed.domain.entity.JudgeOfUser;
import com.yc.feed.domain.entity.OrderCommentUserTag;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.req.AddJudgementReq;
import com.yc.feed.domain.req.GetJudgementReq;

import java.util.List;

/**
 * Created by yusong on 2016/11/2.
 * 司机评价用户服务
 */
public interface JudgementService {

    /*
    *新增用户评价
    */
    public boolean insertJudgement( AddJudgementReq addJudgementReq) throws FeedException;

    /*
    *新增评价标签
    */
    public boolean insertJudgementTag(WebAddJudgementTagReq webAddJudgementTagReq) throws FeedException;

    /*
    *获取司机对用户评价
    */
    public GetJudgementListRes getList(GetJudgementReq getJudgementReq);

    /*
    *获取司机评价用户标签
    */
    public List<OrderCommentUserTag> getTagList(Long orderId);


}
