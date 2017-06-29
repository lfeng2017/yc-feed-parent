package com.yc.feed.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yc.feed.api.http.req.AddCommentTagReq;
import com.yc.feed.api.http.req.GetTagsReq;
import com.yc.feed.dao.crm.DriverMapper;
import com.yc.feed.dao.crm.DriverTagStatMapper;
import com.yc.feed.domain.entity.DriverTagStat;
import com.yc.feed.domain.model.CommentTagModel;
import com.yc.feed.api.util.DateUtil;
import com.yc.feed.dao.mapper.CommentTagMapper;
import com.yc.feed.dao.mapper.OrderCommentTagMapper;
import com.yc.feed.domain.entity.CommentTag;
import com.yc.feed.domain.entity.OrderCommentTag;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.enums.ValidateTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.CommentTagResults;
import com.yc.feed.domain.req.UpdateTagsReq;
import com.yc.feed.service.HistoryEvalRateService;
import com.yc.feed.service.RedisService;
import com.yc.feed.service.TagService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by yusong on 2016/10/8.
 * 用户评价标签服务实现
 */
@Service
public class TagServiceImpl implements TagService , InitializingBean {

    private final Logger logger = Logger.getLogger(TagServiceImpl.class);

    //标签缓存Map key
    private static final String TAGS_MAP_REDIS_KEY = "yc:feed:tags:map";
    //标签文案缓存Map key
    private static final String TAGS_TEXT_MAP_REDIS_KEY = "yc:feed:tags:text:map";
    //标签对象缓存Map key
    private static final String TAGS_MODEL_MAP_REDIS_KEY = "yc:feed:tags:model:map";
    //所有有效标签列表 key
    private static final String TAGS_ALL_AVAILABLE_TAG_LIST = "yc:feed:tags:all:available";
    //所有差评标签列表
    private static final String TAGS_ALL_NEGATIVE_AVAILABLE_TAG_LIST = "yc:feed:tags:negative:available";
    //司机的top3 tags
    private static final String DRIVER_TOP3_TAGS = "yc_feed_driver_top3_tags_";

    @Autowired
    private RedisService<CommentTagModel> redisService;

    @Resource
    private OrderCommentTagMapper orderCommentTagMapper;

    @Resource
    private CommentTagMapper commentTagMapper;

    @Resource
    private DriverTagStatMapper driverTagStatMapper;

    @Autowired
    private HistoryEvalRateService historyEvalRateService;

    @Resource
    private DriverMapper driverMapper;

    @Override
    public ValidateTypes getFinalValuate(List<Integer> commentTagIds) {
        if(null ==commentTagIds || commentTagIds.size() == 0){
            return ValidateTypes.Mediocrity;
        }
        boolean allPositive = true;
        for(Integer tagId :commentTagIds){
            if (null != tagId) {
                byte val = getTagValuate(tagId);
                if (ValidateTypes.Negative.getTypeCode() == val) {
                    return ValidateTypes.Negative;
                }
                if (ValidateTypes.Positive.getTypeCode() != val) {
                    allPositive = false;
                }
            }
        }
        if(allPositive){
            return ValidateTypes.Positive;
        }
        return ValidateTypes.Mediocrity;
    }


    /*
    *插入订单标签记录
    */
    @Override
    @Transactional(rollbackFor =FeedException.class)
    public void insertOrderTag(long orderId,int driverID,int tagId) throws FeedException{
        OrderCommentTag record = new OrderCommentTag();
        record.setServiceOrderId(orderId);
        record.setDriverId(driverID);
        record.setCommentTagId(tagId);
        record.setTagText(getTagText(tagId));
        record.setCreateTime(DateUtil.getNumber(new Date()));
        record.setType(getTagValuate(tagId));
        try {
            orderCommentTagMapper.insert(record);
            logger.info("insertOrderTag|插入评价标签信息|"+record);
        } catch (Exception e) {
            logger.error("insertOrderTag|插入订单评价标签错误|orderId:"+orderId+"|tagId:"+tagId,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),"插入订单标签错误|");
        }
        //更新driver_comment_tag_statistics司机评价标签数量
        updateTagStat(driverID,tagId);
    }

    /*
    *更新标签统计信息
    */
    private void updateTagStat(int driverID,int tagId){
        DriverTagStat record = new DriverTagStat();
        record.setDriverId((long)driverID);
        record.setCommentTagId(tagId);
        logger.info("updateTagStat|更新司机标签统计|driverID:"+driverID+"|tagId:"+tagId+"|");
        int row = driverTagStatMapper.addCount(record);
        logger.info("updateTagStat|更新司机标签统计|driverID:"+driverID+"|tagId:"+tagId+"|OK|row:"+row);
        if(row>0){
            logger.info("updateTagStat|更新司机标签统计|累加成功|");
            return;
        }
        record.setTagText(getTagText(tagId));
        CommentTagModel tagModel = getTag(tagId);
        if (null != tagModel){
            record.setType(tagModel.getType());
        }else{
            record.setType((byte)0);
        }
        record.setCount(1);
        record.setCreateTime(DateUtil.getNumber(new Date()));
        logger.info("updateTagStat|插入新记录|"+record);
        try {
            driverTagStatMapper.insert(record);
        } catch (Exception e) {
            logger.error("updateTagStat|插入新记录异常|"+record,e);
        }
    }


    @Override
    public List<CommentTagModel> getAllCommentTag() {
        List<CommentTagModel> tags = redisService.getList(TAGS_ALL_AVAILABLE_TAG_LIST);
        if(null == tags){
            logger.warn("getAllCommentTag|缓存为空|");
            tags = cacheAvailableTags();
        }
        return tags;
    }

    @Override
    public List<CommentTagModel> getAllNegativeCommentTag() {
        List<CommentTagModel> tags = redisService.getList(TAGS_ALL_NEGATIVE_AVAILABLE_TAG_LIST);
        if(null == tags){
            logger.warn("getAllNegativeCommentTag|缓存为空|");
            tags = cacheNegativeTags();
        }
        return tags;
    }

    @Override
    public List<CommentTagModel> getOrderTags(Long orderId) {
        List<OrderCommentTag> orderCommentTags = orderCommentTagMapper.getByOrderId(orderId);
        List<CommentTagModel> models = new ArrayList<CommentTagModel>();
        for (OrderCommentTag orderCommentTag : orderCommentTags){
            CommentTagModel model =getTag(orderCommentTag.getCommentTagId());
            models.add(model);
        }
        return models;
    }

    @Override
    public CommentTagModel getTag(int tagId) {
        String jsonStr = redisService.mapGet(TAGS_MODEL_MAP_REDIS_KEY,String.valueOf(tagId));
        CommentTagModel res = null;
        if (null != jsonStr ){
            Gson gson = new Gson();
            try {
                res = gson.fromJson(jsonStr,CommentTagModel.class);
            } catch (JsonSyntaxException e) {
                logger.error("getTag|对象转化失败:tagId:"+tagId+"|"+jsonStr);
            }
        }else{
            logger.warn("getTag|redis中未查询到指定ID:"+tagId);
            CommentTag tagResult = commentTagMapper.selectByPrimaryKey(tagId);
            res = transfer(tagResult);
            redisService.putObject2Map(TAGS_MODEL_MAP_REDIS_KEY,String.valueOf(tagId),res);
        }
        return res;
    }

    /*
    *获取tag标签对应的分数
    */
    private byte getTagValuate(Integer tagId){
        if(null == tagId){
            logger.warn("getTagValuate|标签为NULL|"+tagId);
            return 0;
        }
        String type = redisService.mapGet(TAGS_MAP_REDIS_KEY,tagId.toString());
        if (null != type){
            return Byte.parseByte(type);
        }
        CommentTag tag = commentTagMapper.selectByPrimaryKey(tagId);
        if (null != tag){
            redisService.put2Map(TAGS_MAP_REDIS_KEY,String.valueOf(tag.getCommentTagId()),String.valueOf(tag.getType()));
            return tag.getType();
        }
        logger.warn("getTagValuate|标签未找到|"+tagId);
        return 0;
    }

    /*
     *类加载执行数据初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        cacheMap();
        cacheAvailableTags();
        cacheNegativeTags();
    }

    /*
    *缓存标签Map
    */
    private void cacheMap(){
        CommentTag query = new CommentTag();
        List<CommentTag> commentTags = commentTagMapper.list(query);
        Iterator<CommentTag> it = commentTags.iterator();
        while (it.hasNext()){
            CommentTag tag = it.next();
            //插入到缓存
            redisService.put2Map(TAGS_MAP_REDIS_KEY,String.valueOf(tag.getCommentTagId()),String.valueOf(tag.getType()));
            redisService.put2Map(TAGS_TEXT_MAP_REDIS_KEY,String.valueOf(tag.getCommentTagId()),tag.getTagText());
            redisService.putObject2Map(TAGS_MODEL_MAP_REDIS_KEY,String.valueOf(tag.getCommentTagId()),transfer(tag));
        }
        logger.info("cacheMap|初始化标签MAP缓存完成|记录总数："+commentTags.size());
    }

    /*
    *缓存有效标签
    */
    private List<CommentTagModel> cacheAvailableTags(){
        CommentTag query = new CommentTag();
        query.setStatus((byte)1);
        List<CommentTag> availableTags = commentTagMapper.list(query);
        List<CommentTagModel> res = transferList(availableTags);
        redisService.saveList(TAGS_ALL_AVAILABLE_TAG_LIST,res);
        logger.info("cacheMap|初始化有效标签列表缓存完成|记录总数："+res.size());
        return res;
    }

    /*
    *缓存负面标签列表
    */
    private List<CommentTagModel> cacheNegativeTags(){
        List<CommentTag> negativeTags = commentTagMapper.listNegative();
        List<CommentTagModel> res = transferList(negativeTags);
        redisService.saveList(TAGS_ALL_NEGATIVE_AVAILABLE_TAG_LIST,res);
        logger.info("cacheNegativeTags|初始化负面标签列表缓存完成|记录总数："+res.size());
        return res;
    }

    /*
    *将数据库记录包装为对外对象
    */
    private List<CommentTagModel> transferList(List<CommentTag> availableTags){
        List<CommentTagModel> res = new ArrayList<CommentTagModel>();
        if(null != availableTags || availableTags.size() > 0){
            for (CommentTag entity: availableTags){
                CommentTagModel model = transfer(entity);
                res.add(model);
            }
        }
        return res;
    }

    private CommentTagModel transfer(CommentTag entity){
        if (null == entity){
            return null;
        }
        CommentTagModel model = new CommentTagModel();
        model.setCommentTagId(entity.getCommentTagId());
        model.setConflictTagId(entity.getConflictTagId());
        model.setTagText(entity.getTagText());
        model.setScore(entity.getScore());
        model.setType(entity.getType());
        model.setStatus(entity.getStatus());
        model.setRank(entity.getRank());
        model.setFlag(entity.getFlag());
        model.setCreateTime(entity.getCreateTime());
        model.setOperatorId(entity.getOperatorId());
        return model;
    }



    /*
    *获取tag标签对应的文案
    */
    private String getTagText(Integer tagId){
        String text = redisService.mapGet(TAGS_TEXT_MAP_REDIS_KEY,tagId.toString());
        if (null != text){
            return text;
        }
        CommentTag tag = commentTagMapper.selectByPrimaryKey(tagId);
        if (null != tag){
            redisService.put2Map(TAGS_TEXT_MAP_REDIS_KEY,String.valueOf(tag.getCommentTagId()),String.valueOf(tag.getType()));
            return tag.getTagText();
        }
        logger.warn("getTagValuate|标签未找到|"+tagId);
        return null;
    }

    @Override
    public List<CommentTagModel> getByQueryString(GetTagsReq query) throws FeedException{
        logger.info("getByQueryString|标签请求信息："+query);
        List<CommentTagModel> res = new ArrayList<>();
        try{
            List<CommentTag> list = commentTagMapper.listByQueryString(query);
            res = transferList(list);
            logger.info("getByQueryString|获取标签信息完成|获取标签信息:" +res);
            return res;
        }catch (Exception e){
            logger.error("getByQueryString|获取标签信错误"+query+"|"+e.getMessage());
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }
    }

    @Override
    public void update(UpdateTagsReq updateReq) throws FeedException {
        logger.info("update|receive update req:"+updateReq);
        CommentTag tag = commentTagMapper.selectByPrimaryKey(Integer.valueOf(updateReq.getCommentTagId()));
        if (null == tag){
            logger.error("update|404|record not founded!|update:"+updateReq);
            throw new FeedException(CodeTypes.NoRecord.getCode(),CodeTypes.NoRecord.getMsg());
        }

        //更新状态
        if (updateReq.getStatus() != null) {
            int status = Integer.valueOf(updateReq.getStatus());
            tag.setStatus((byte)status);
        }
        //更新互斥标签
        if (updateReq.getConflictTagId() != null) {
            tag.setConflictTagId(Integer.valueOf(updateReq.getConflictTagId()));
        }

        //更新分值
        if (updateReq.getScore() != null) {
            tag.setScore((byte)Integer.valueOf(updateReq.getScore()).intValue());
        }

        //更新排名
        if (updateReq.getRank() != null) {
            tag.setRank(Integer.valueOf(updateReq.getRank()));
        }

        if (updateReq.getFlag() != null) {
            tag.setFlag(Integer.valueOf(updateReq.getFlag()));
        }

        tag.setUpdateTime(DateUtil.getNumber(new Date()));

        try {
            int count = commentTagMapper.updateByPrimaryKey(tag);
            logger.info("update|success|"+tag+"|count:"+count);

            if (count != 0){
                afterPropertiesSet();
                logger.info("redis update |success|"+tag+"|CommentTagId:"+tag.getCommentTagId());
            }
        } catch (Exception e) {
            logger.error("update|500|update error|updateReq:"+tag);
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }

    }

    @Override
    public void insertTag(AddCommentTagReq insert) throws FeedException {

        logger.info("insertTag|receive insert req:"+insert);

        CommentTag tag = new CommentTag();
        tag.setConflictTagId(insert.getConflictTagId());
        tag.setTagText(insert.getTagText());
        int score = insert.getScore();
        tag.setScore((byte)score);
        int type = insert.getType();
        tag.setType((byte)type);
        int status = insert.getStatus();
        tag.setStatus((byte)status);
        tag.setRank(insert.getRank());
        tag.setOperatorId(insert.getOperatorId());
        tag.setCreateTime(DateUtil.getNumber(new Date()));
        tag.setUpdateTime(DateUtil.getNumber(new Date()));
        tag.setFlag(insert.getFlag());

        try {
            int count = commentTagMapper.insert(tag);
            logger.info("insert|success|"+tag+"|count:"+count);
        } catch (Exception e) {
            logger.error("insert|500|update error|insertReq:"+tag+"|"+e.getMessage());
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }
    }


    @Override
    public HashMap<String,String> getTop3Tags(String driverIds) throws Exception{
        String[] driverArrs = driverIds.split(",");
        Gson gson = new Gson();
        HashMap<String,String> resultTags = new HashMap<>();
        for(String driverId : driverArrs){
            String top3Tags = redisService.getStr(DRIVER_TOP3_TAGS + driverId);
            resultTags.put(driverId,top3Tags);
        }
        return resultTags;
    }


    public void runDriverTop3Tags(int shardTotal,int currentShard){
        long startTime =  System.currentTimeMillis();
        List<Long> allVailDrivers = driverMapper.getAllValidDriverIds(shardTotal,currentShard);
        logger.info("runDriverTop3Tags|获得有效的司机|Count:"+ allVailDrivers.size());
        for(Long driverId : allVailDrivers){
            try {
                String[] orders = historyEvalRateService.getLastOrder(driverId);
                //根据司机最近50单，获取所有的评价标签和标签出现的次数,top3
                List<CommentTagResults> tagStatResults = orderCommentTagMapper.getByOrderIds(orders);
                redisService.put(DRIVER_TOP3_TAGS + driverId,tagStatResults,60*60*25);
            } catch (FeedException e) {
                logger.error("runDriverTop3Tags|"+driverId+"获取Top3 Tags查询异常"+e.getMessage());
                continue;
            }
        }
        logger.info("runDriverTop3Tags|更新全部司机的Top3 Tags完成|:"+(System.currentTimeMillis()-startTime)+"ms");
    }
}
