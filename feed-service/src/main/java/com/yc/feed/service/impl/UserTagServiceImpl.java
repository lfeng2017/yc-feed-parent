package com.yc.feed.service.impl;

import com.yc.feed.dao.crm.CommentUserTagMapper;
import com.yc.feed.domain.entity.CommentUserTag;
import com.yc.feed.domain.model.CommentUserTagModel;
import com.yc.feed.service.RedisService;
import com.yc.feed.service.UserTagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lfeng on 2016/11/30.
 * 司机给用户评价的评价标签服务实现
 */
@Service
public class UserTagServiceImpl implements UserTagService, InitializingBean {

    private final Logger logger = Logger.getLogger(UserTagServiceImpl.class);

    //所有有效标签列表 key
    private static final String USERTAGS_ALL_AVAILABLE_TAG_LIST = "yc:feed:usertags:all:available";

    @Resource
    private CommentUserTagMapper commentUserTagMapper;

    @Autowired
    private RedisService<CommentUserTagModel> redisService;


    @Override
    public List<CommentUserTagModel> getAllUserCommentTag() {
        List<CommentUserTagModel> tags = redisService.getList(USERTAGS_ALL_AVAILABLE_TAG_LIST);
        if(null == tags){
            logger.warn("getAllNegativeCommentTag|缓存为空|");
            tags = cacheAvailableTags();
        }
        return tags;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cacheAvailableTags();
    }

    /*
       *缓存有效标签
       */
    private List<CommentUserTagModel> cacheAvailableTags(){
        CommentUserTag query = new CommentUserTag();
        List<CommentUserTag> commentUserTags = commentUserTagMapper.list(query);
        List<CommentUserTagModel> res = transferList(commentUserTags);
        redisService.saveList(USERTAGS_ALL_AVAILABLE_TAG_LIST,res);
        logger.info("cacheMap|初始化司机给用户的评价标签列表缓存完成|记录总数："+res.size());
        return res;
    }

    /*
    *将数据库记录包装为对外对象
    */
    private List<CommentUserTagModel> transferList(List<CommentUserTag> availableTags){
        List<CommentUserTagModel> res = new ArrayList<CommentUserTagModel>();
        if(null != availableTags || availableTags.size() > 0){
            for (CommentUserTag entity: availableTags){
                CommentUserTagModel model = transfer(entity);
                res.add(model);
            }
        }
        return res;
    }

    private CommentUserTagModel transfer(CommentUserTag entity){
        if (null == entity){
            return null;
        }
        CommentUserTagModel model = new CommentUserTagModel();
        model.setCommentUserTagId(entity.getCommentUserTagId());
        model.setConflictTagId(entity.getConflictTagId());
        model.setTagText(entity.getTagText());
        model.setType(entity.getType());
        model.setGender(entity.getGender());
        return model;
    }
}
