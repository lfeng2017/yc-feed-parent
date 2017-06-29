package com.yc.feed.dao.crm;

import com.yc.feed.domain.entity.JudgeOfUser;
import com.yc.feed.domain.req.GetJudgementReq;

import java.util.List;

public interface JudgeOfUserMapper {
    int deleteByPrimaryKey(Long djouId);

    int insert(JudgeOfUser record);

    int insertSelective(JudgeOfUser record);

    JudgeOfUser selectByPrimaryKey(Long djouId);

    int updateByPrimaryKeySelective(JudgeOfUser record);

    int updateByPrimaryKey(JudgeOfUser record);

    List<JudgeOfUser> list(GetJudgementReq getJudgementReq);

    Long count(GetJudgementReq getJudgementReq);

}