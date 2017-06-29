package com.yc.feed.service.impl;

import com.yc.feed.api.http.req.WebAddJudgementTagReq;
import com.yc.feed.api.http.res.GetJudgementListRes;
import com.yc.feed.api.util.DateUtil;
import com.yc.feed.dao.crm.JudgeOfUserMapper;
import com.yc.feed.dao.crm.OrderCommentUserTagMapper;
import com.yc.feed.domain.entity.JudgeOfUser;
import com.yc.feed.domain.entity.OrderCommentUserTag;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.JudgeOfUserModel;
import com.yc.feed.domain.req.AddJudgementReq;
import com.yc.feed.domain.req.GetJudgementReq;
import com.yc.feed.service.JudgementService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yusong on 2016/11/2.
 * 司机评价用户服务实现
 */
@Service
public class JudgementServiceImpl implements JudgementService {

    private final Logger logger = Logger.getLogger(JudgementServiceImpl.class);


    @Resource
    private JudgeOfUserMapper judgeOfUserMapper;

    @Resource
    private OrderCommentUserTagMapper orderCommentUserTagMapper;

    @Override
    public boolean insertJudgement(AddJudgementReq addJudgementReq) throws FeedException {
        JudgeOfUser record = new JudgeOfUser();
        record.setServiceOrderId(addJudgementReq.getOrderId());
        record.setDriverId(addJudgementReq.getDriverId());
        record.setUserId(addJudgementReq.getUserId());
        record.setScore(addJudgementReq.getScore());
        record.setDetail(addJudgementReq.getDetail());
        record.setCreateTime((long)DateUtil.getNumber(new Date()));
        logger.info("insertJudgement|新增司机评价|"+record);
        try {
            judgeOfUserMapper.insert(record);
        } catch (Exception e) {
            logger.error("insertJudgement|新增司机评价|req:"+addJudgementReq,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),"新增司机评价错误");
        }
        return Boolean.TRUE;
    }


    @Override
    public boolean insertJudgementTag(WebAddJudgementTagReq webAddJudgementTagReq) throws FeedException {
        OrderCommentUserTag record = new OrderCommentUserTag();
        record.setServiceOrderId(webAddJudgementTagReq.getServiceOrderId());
        record.setDriverId(webAddJudgementTagReq.getDriverId());
        record.setUserId(webAddJudgementTagReq.getUserId());
        record.setCommentUserTagId(webAddJudgementTagReq.getCommentUserTagId());
        record.setTagText(webAddJudgementTagReq.getTagText());
        record.setType(webAddJudgementTagReq.getType());
        record.setCreateTime(DateUtil.getNumber(new Date()));
        logger.info("insertJudgementTag|插入司机评价乘客标签|"+record);
        try {
            orderCommentUserTagMapper.insert(record);
        } catch (Exception e) {
            logger.error("insertJudgementTag|插入司机评价乘客标签错误|"+record,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),"insert error");
        }
        return Boolean.TRUE;
    }

    @Override
    public GetJudgementListRes getList(GetJudgementReq getJudgementReq) {
        List<JudgeOfUser> entities = judgeOfUserMapper.list(getJudgementReq);
        Long total = judgeOfUserMapper.count(getJudgementReq);
        List<JudgeOfUserModel> models = new ArrayList<JudgeOfUserModel>();
        for (JudgeOfUser entity: entities){
            JudgeOfUserModel model = transfer(entity);
            models.add(model);
        }
        GetJudgementListRes res = new GetJudgementListRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),total,models.size(),models);
        return res;
    }

    @Override
    public List<OrderCommentUserTag> getTagList(Long orderId) {
        return orderCommentUserTagMapper.getByOrderId(orderId);
    }

    private JudgeOfUserModel transfer(JudgeOfUser entity){
        if(null == entity){
            return null;
        }
        JudgeOfUserModel model = new JudgeOfUserModel();
        model.setId(entity.getDjouId());
        model.setUserId(entity.getUserId());
        model.setDriverId(entity.getDriverId());
        model.setServiceOrderId(entity.getServiceOrderId());
        model.setScore(entity.getScore());
        model.setDetail(new String(entity.getDetail()));
        model.setCreateTime(entity.getCreateTime());
        return model;
    }



}
