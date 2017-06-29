package com.yc.feed.service;

import com.yc.feed.domain.model.CommentUserTagModel;

import java.util.List;

/**
 * Created by lfeng on 2016/11/30.
 * 用户评价标签服务
 */
public interface UserTagService {

    /*
    *获取所有标签
    */
    public List<CommentUserTagModel> getAllUserCommentTag();

}
