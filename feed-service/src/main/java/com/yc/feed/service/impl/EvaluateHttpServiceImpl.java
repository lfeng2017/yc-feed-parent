package com.yc.feed.service.impl;

import com.google.gson.Gson;
import com.le.config.client.ConfigManager;
import com.yc.feed.api.constant.RedisKey;
import com.yc.feed.api.http.req.DriverMQInfo;
import com.yc.feed.api.http.req.EvaluationInfo;
import com.yc.feed.api.http.req.EvaluationRateReq;
import com.yc.feed.api.http.res.AddCommentRes;
import com.yc.feed.api.http.res.EvaluationRateRes;
import com.yc.feed.api.http.res.MonthsEvaluationRes;
import com.yc.feed.api.util.*;
import com.yc.feed.dao.crm.CompletedOrderMapper;
import com.yc.feed.dao.mapper.OrderCommentMapper;
import com.yc.feed.dao.shard.FeedActivityEvaluationMangoDao;
import com.yc.feed.domain.config.DictConfig;
import com.yc.feed.domain.entity.CompletedOrder;
import com.yc.feed.domain.entity.FeedActivityEvaluationInfo;
import com.yc.feed.domain.entity.OrderComment;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.enums.TempOrderStatus;
import com.yc.feed.domain.enums.ValidateTypes;
import com.yc.feed.domain.excep.FeedException;
import com.yc.feed.domain.model.CommentTagModel;
import com.yc.feed.domain.model.MonthsEvalutionInfo;
import com.yc.feed.domain.model.OrderCommentInfo;
import com.yc.feed.domain.model.OrderInfo;
import com.yc.feed.domain.req.AddCommentReq;
import com.yc.feed.domain.req.ChangeCommentStatusReq;
import com.yc.feed.domain.req.GetCommentListReq;
import com.yc.feed.service.*;
import com.yongche.consumer.rabbitmq.utils.phprpc.PHPSerializerUtil;

import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by yusong on 2016/10/13.
 * 用户评价HTTP服务
 */
@Service
public class EvaluateHttpServiceImpl implements EvaluateHttpService {

    private final Logger logger = Logger.getLogger(EvaluateHttpServiceImpl.class);

    @Resource
    private AmqpTemplate amqpTemplateComment;

    @Resource
    private AmqpTemplate amqpTemplateDriver;

    @Resource
    private OrderCommentMapper orderCommentMapper;

    @Resource
    private CompletedOrderMapper completedOrderMapper;

    @Resource
    private FeedActivityEvaluationMangoDao feedActivityEvaluationMangoDao;

    @Autowired
    private TagService tagService;

    @Autowired
    private RedisService<OrderComment> redisService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private WhiteListService whiteListService;

    @Autowired
    private HistoryEvalRateService historyEvalRateService;

    private static final String CONTENT_TEXT = "有乘客对您的服务给出了很高的评价，请再接再厉！";

    @Override
    @Transactional(rollbackFor =FeedException.class)
    public AddCommentRes addUserComment(AddCommentReq addCommentReq) throws FeedException{
        long driverId = addCommentReq.getOrderInfo().getDriverId();
        long orderId = addCommentReq.getOrderId();
        //0.计算评价.
        ValidateTypes val = tagService.getFinalValuate(addCommentReq.getCommentTagIds());
        //1.更新数据库（原有数据库）
        //1.1如果为差评查询是否为白名单
        if(val.getTypeCode() == ValidateTypes.Negative.getTypeCode()){
            boolean beWhite = false ;
            try {
                beWhite = whiteListService.beWhiteList(driverId);
            } catch (Exception ignore) {
                logger.error("addUserComment|",ignore);
            }
            if(beWhite){
                logger.info("addUserComment|白名单评价|差评不处理|driverId:"+driverId);
                boolean updateOk = platformService.updateOrder(orderId,Boolean.TRUE);
                return new AddCommentRes(Boolean.TRUE,CodeTypes.Success.getCode(),CodeTypes.Success.getMsg());
            }
        }
        //1.2新增评价记录
        try {
            addCommentRecord(addCommentReq,val);
        } catch (Exception e) {
            logger.error("addUserComment|新增订单评价错误|"+addCommentReq.getOrderId());
            throw new FeedException(CodeTypes.RepeatError.getCode(),CodeTypes.RepeatError.getMsg());
        }
        //2.更新评价标签信息
        List<Integer> commentTagIds = addCommentReq.getCommentTagIds();
        if(null != commentTagIds && 0 != commentTagIds.size()){
            for(Integer tagId : commentTagIds){
                if (null != tagId) {
                    tagService.insertOrderTag(addCommentReq.getOrderId(), addCommentReq.getOrderInfo().getDriverId(), tagId);
                }
            }
        }else{
            logger.warn("addUserComment|订单不包含标签|");
        }

        //3.通知平台更新标志
        boolean updateOk = platformService.updateOrder(orderId,Boolean.TRUE);
        if(!updateOk){
            logger.error("evaluate|更新平台订单失败|orderId:"+orderId);
            //订单更新失败数据库回滚
            throw new FeedException( CodeTypes.SystemError.getCode(), CodeTypes.SystemError.getMsg());
        }
        //php 时间戳是秒级
        Date date = new Date(addCommentReq.getOrderInfo().getEndTime()*1000);
        boolean appendOk = platformService.appendTrack(addCommentReq.getOrderId(),addCommentReq.getUserId(),new Date());
        if(!appendOk){
            logger.error("evaluate|追加评价轨迹失败|orderId:"+orderId);
            //追加记录失败，回滚订单状态
            boolean rollbackOrder = platformService.updateOrder(addCommentReq.getOrderId(),Boolean.FALSE);
            logger.error("evaluate|订单状态回滚|orderId:"+orderId+"|status:"+rollbackOrder);
            throw new FeedException( CodeTypes.SystemError.getCode(), CodeTypes.SystemError.getMsg());
        }
        //发送司机端通知（短信）
        if(ValidateTypes.Positive.getTypeCode() == val.getTypeCode()){
            platformService.sendSMS(driverId,CONTENT_TEXT);
        }
        //4.加入到队列（更新活动好评率）
        try {
            sendMQ(addCommentReq,val);
        } catch (Exception e) {
            throw new FeedException( CodeTypes.SystemError.getCode(), CodeTypes.SystemError.getMsg());
        }
        //mq通知司机端(增加司机经验值)
        sendDriverMQ(addCommentReq,val);

        //5.更新好评率
        try {
            historyEvalRateService.updateDriverRate(driverId,orderId,val,false);
        } catch (Exception ignore) {
            logger.error("evaluate|更新好评率异常|orderId:"+orderId,ignore);
        }
        //6.返回结果
        return new AddCommentRes(Boolean.TRUE,CodeTypes.Success.getCode(),CodeTypes.Success.getMsg());
    }

    public void sendCommentToMonitor(OrderComment orderComment,List<Integer> commentTagIds){
        long driverId = orderComment.getDriverId();
        String driverCity = orderComment.getCity();
        Gson gson = new Gson();
        Map<String, Object> params =  new HashMap<String, Object>();
        params.put("driver_id",driverId);
        //vehicle_number车牌号
        String driverVehicleNumber ="";
        //identity_card身份证号
        String driverIdentityCard ="";
        try {
            //根据司机id获取身份证号和车牌号
            String requestUrl = ConfigManager.get(DictConfig.class).getDictMap().get("driverInfoUrl");
            String res = HttpClientUtil.httpGetRequest(requestUrl,params);
            Map<String,Object> resultMap = gson.fromJson(res,Map.class);
            if("success".equals(resultMap.get("ret_msg"))){
                Map<String,String> resultValue = (Map<String, String>) resultMap.get("result");
                if(resultValue!=null){
                    driverVehicleNumber = resultValue.get("vehicle_number");
                    driverIdentityCard = resultValue.get("identity_card");
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if(driverCity.equals("bj") && Validator.isBjCar(driverVehicleNumber) && Validator.isBjPerson(driverIdentityCard)){
            String requesMonitortUrl = "http://114.251.208.169/info/app/common/binapi/bjdfcyxxjsyxgs";
            HashMap<String,Object> requestParam = new HashMap<String,Object>();
            long orderNo = orderComment.getServiceOrderId();
            StringBuffer commentType = new StringBuffer();
            if(null != commentTagIds && 0 != commentTagIds.size()){
                for(Integer tagId : commentTagIds){
                    if (null != tagId) {
                        CommentTagModel model = tagService.getTag(tagId);
                        commentType.append(model.getTagText()+",");
                    }
                }
            }
            commentType.append(orderComment.getContent());

            Byte evaluation = orderComment.getEvaluation();
            int evaluationValue = 5;
            if(evaluation == -1){
                evaluationValue = 1;
            }else if(evaluation ==0){
                evaluationValue = 3;
            }

            requestParam.put("orderNo",orderNo);
            requestParam.put("evaluateTime",DateUtil.getDateStr(orderComment.getCreateTime()));
            requestParam.put("driverEvaluate",evaluationValue);
            requestParam.put("vehicleEvaluate",evaluationValue);
            requestParam.put("serviceEvaluate",evaluationValue);
            requestParam.put("commentType",commentType.toString());
            requestParam.put("complainTime","");
            requestParam.put("complain","");
            requestParam.put("complainResult","");

            StringBuilder paramBuilder = new StringBuilder("{\"items\":[");
            paramBuilder.append(gson.toJson(requestParam));
            paramBuilder.append("]}");

            String fileName = "BJDFCYXXJSYXGS_CKPJ_REQ_"+DateUtil.getDateStrMs()+".json";
            try {
                String res = HttpClientUtil.post(requesMonitortUrl,fileName,paramBuilder.toString());
                logger.info("=========向监管平台传输数据成功"+res);
            } catch (Exception e) {
                logger.error("=======向监管平台传输数据错误",e);
            }
        }
    }


    public void sendCommentToMonitor_new(OrderComment orderComment,List<Integer> commentTagIds){
        long driverId = orderComment.getDriverId();
        String driverCity = orderComment.getCity();
        Gson gson = new Gson();
        Map<String, Object> params =  new HashMap<String, Object>();
        params.put("driver_id",driverId);
        //vehicle_number车牌号
        String driverVehicleNumber ="";
        //identity_card身份证号
        String driverIdentityCard ="";
        try {
            //根据司机id获取身份证号和车牌号
            String requestUrl = ConfigManager.get(DictConfig.class).getDictMap().get("driverInfoUrl");
            String res = HttpClientUtil.httpGetRequest(requestUrl,params);
            Map<String,Object> resultMap = gson.fromJson(res,Map.class);
            if("success".equals(resultMap.get("ret_msg"))){
                Map<String,String> resultValue = (Map<String, String>) resultMap.get("result");
                if(resultValue!=null){
                    driverVehicleNumber = resultValue.get("vehicle_number");
                    driverIdentityCard = resultValue.get("identity_card");
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HashMap<String,Object> requestParam = new HashMap<String,Object>();
        long orderNo = orderComment.getServiceOrderId();
        StringBuffer commentType = new StringBuffer();
        if(null != commentTagIds && 0 != commentTagIds.size()){
            for(Integer tagId : commentTagIds){
                if (null != tagId) {
                    CommentTagModel model = tagService.getTag(tagId);
                    commentType.append(model.getTagText()+",");
                }
            }
        }
        commentType.append(orderComment.getContent());

        Byte evaluation = orderComment.getEvaluation();
        int evaluationValue = 5;
        if(evaluation == -1){
            evaluationValue = 1;
        }else if(evaluation ==0){
            evaluationValue = 3;
        }

        requestParam.put("orderNo",orderNo);
        requestParam.put("evaluateTime",DateUtil.getDateStr(orderComment.getCreateTime()));
        requestParam.put("driverEvaluate",evaluationValue);
        requestParam.put("vehicleEvaluate",evaluationValue);
        requestParam.put("serviceEvaluate",evaluationValue);
        requestParam.put("commentType",commentType.toString());
        requestParam.put("complainTime","");
        requestParam.put("complain","");
        requestParam.put("complainResult","");
        requestParam.put("driverVehicleNumber",driverVehicleNumber);
        requestParam.put("driverIdentityCard",driverIdentityCard);
        requestParam.put("driverCity",driverCity);

        try {
            FileUtil.writeDataToFile(gson.toJson(requestParam));
            logger.info("========sendCommentToMonitor_new|写入评论到本地文件成功=============="+orderNo);
        } catch (Exception e) {
            logger.error("=======sendCommentToMonitor_new|写入评论到本地文件报错========="+e.getMessage());
        }
    }
    @Override
    public EvaluationRateRes  getDriverEvaluation(EvaluationRateReq evaluationRateReq) {
        logger.info("getDriverEvaluation|收到获取好评率请求:"+evaluationRateReq);
        String redisKey = RedisKeyUtil.getFeedbackRateKey(evaluationRateReq.getActivityId(),evaluationRateReq.getDriverId());
        /*String rateStr = redisService.mapGet(redisKey, RedisKey.EVALUATION_MAP_KEY);
        String orderIdStr = redisService.mapGet(redisKey, RedisKey.ORDER_ID_MAP_KEY);*/
        String rateStr ="";
        String orderIdStr="";
        int rate = 0;
        long orderId = 0;
        EvaluationRateRes res = null;
        if (!StringUtils.isEmpty(rateStr)) {
            try {
                rate = Integer.parseInt(rateStr);
                orderId = Long.parseLong(orderIdStr);
            } catch (NumberFormatException e) {
                logger.error("getRate|好评率格式错误|redisKey：" + redisKey + "|rateStr:" + rateStr, e);
               // return new EvaluationRateRes(Boolean.FALSE, "-1", "好评率格式错误", orderId, rate);
            }
            res = new EvaluationRateRes(Boolean.TRUE, "0", "成功", orderId, rate);
        }else{
            logger.warn("getDriverEvaluation|未获取到缓存数据|redisKey："+redisKey);
        }
        if (null == res){
            res = getEvalFromDB(evaluationRateReq);
        }
        if (null == res){
            logger.warn("getDriverEvaluation|未获取到数据库数据|redisKey："+redisKey);
            res = new  EvaluationRateRes(Boolean.TRUE,"0","司机尚未开始参加活动",orderId,rate);
        }
        logger.info("getRate|收到获取好评率响应:"+res);
        return res;
    }

    @Override
    public OrderCommentInfo getByOrderId(Long orderId) throws FeedException{
        OrderComment entity = null;
        try {
            entity = orderCommentMapper.selectByOrderId(orderId);
        } catch (Exception e) {
            logger.error("getByOrderId|查询订单评价错误|orderId:"+orderId,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),"查询订单评价出错");
        }
        if(null == entity){
            throw new FeedException(CodeTypes.NoRecord.getCode(),CodeTypes.NoRecord.getMsg());
        }
        OrderCommentInfo model = transfer(entity);
        List<CommentTagModel> tags = tagService.getOrderTags(model.getOrderId());
        model.setTags(tags);
        logger.info("getByOrderId|查询到一条评价:"+model);
        return model;
    }

    @Override
    public void changeStatus(ChangeCommentStatusReq changeCommentStatusReq)throws FeedException {
        Long orderId = changeCommentStatusReq.getOrderId();
        if (null == getByOrderId(orderId)){
            logger.warn("changeStatus|未查询到记录|"+orderId);
            throw new FeedException(CodeTypes.NoRecord.getCode(),CodeTypes.NoRecord.getMsg());
        }
        OrderComment record = new OrderComment();
        record.setOperatorId(changeCommentStatusReq.getOperatorId());
        record.setServiceOrderId(orderId);
        record.setDriverId(changeCommentStatusReq.getDriverId());
        if (changeCommentStatusReq.isStatus()){
            record.setStatus((byte)1);
        }else{
            record.setStatus((byte)0);
        }
        record.setUpdateTime(DateUtil.getNumber(new Date()));
        logger.info("changeStatus|更新评价单状态|"+record);
        try {
            orderCommentMapper.updateStatus(record);
            logger.info("changeStatus|更新评价单状态OK|"+changeCommentStatusReq);
        } catch (Exception e) {
            logger.error("changeStatus|更新评价单状态错误|"+changeCommentStatusReq,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }
        //更新好评率
        historyEvalRateService.updateRateByStatus((long)record.getDriverId(),orderId,changeCommentStatusReq.isStatus());
    }


    @Override
    public List<OrderCommentInfo> getCommentList(GetCommentListReq req) {
        List<OrderCommentInfo> res = new ArrayList<OrderCommentInfo>();

        List<OrderComment> entities = new ArrayList<OrderComment>();
        OrderComment comment = null;
        if (req.getOrderIds() != null && req.getOrderIds().length() > 0) {
            String[] ids = req.getOrderIds().split(",");
            for (String str : ids) {
                comment = new OrderComment();
                comment = orderCommentMapper.selectByOrderId(Long.parseLong(str));
                logger.info("getCommentList|通过订单id查询|"+comment + ":" + str );
                if (comment != null) {
                    entities.add(comment);
                }
            }
        } else {
            entities = orderCommentMapper.list(req);
        }
        if(null == entities || entities.size() == 0){
            logger.info("getCommentList|未查询到评价记录|"+req);
        } else  {
            for (OrderComment entity : entities) {
                OrderCommentInfo model = transfer(entity);
                if (req.isNeedTag()) {
                    List<CommentTagModel> tags = tagService.getOrderTags(model.getOrderId());
                    model.setTags(tags);
                }
                res.add(model);
            }
        }
        return res;
    }

    @Override
    public long getCount(GetCommentListReq req) {
        return orderCommentMapper.count(req);
    }

    @Override
    public MonthsEvaluationRes getMonthsEvaluation(long startTime, long endTime, String driverIds) {
    	GetCommentListReq req = new GetCommentListReq();
    	String[] ids = driverIds.split(",");
    	List<MonthsEvalutionInfo> monthslist = new ArrayList<MonthsEvalutionInfo>();
    	MonthsEvalutionInfo info = null;
    	StringBuilder badOrderIds = new StringBuilder();
    	for (String driverId:ids) {
            badOrderIds = new StringBuilder();
    		info = new MonthsEvalutionInfo();
    		req.setDriverId(Long.valueOf(driverId));
        	req.setStartTimeStamp(startTime);
        	req.setEndTimeStamp(endTime);
        	List<OrderComment> entities = orderCommentMapper.listByEndTime(req);
        	int goodOrdersCount = 0;
        	int badOrdersCount = 0;
        	if (entities != null && entities.size() > 0) {
	        	for (OrderComment entity : entities) {
	        		if (entity.getStatus() == 1) {
	        			if (entity.getEvaluation() == 1) {
	        				goodOrdersCount++;
	        			} else if (entity.getEvaluation() == -1) {
	        				badOrdersCount++;
	        				badOrderIds.append(entity.getServiceOrderId()).append(",");
	        			}
	        		}
	        	}
	        	int evaluation = 0;
	        	if (goodOrdersCount + badOrdersCount > 0) {
                    evaluation  = (goodOrdersCount * 100) / (goodOrdersCount + badOrdersCount);
                }
                info.setDriverId(driverId);
	        	info.setEvaluation(evaluation);
	        	info.setBadOrdersCount(badOrdersCount);
	        	if (badOrderIds.length() > 0) {
	        		String str = badOrderIds.toString();
	        		info.setBadOrderIds(str.substring(0, str.length() - 1));
	        	} else {
                    info.setBadOrderIds("");
                }
	        	monthslist.add(info);
        	}
    	}
    	MonthsEvaluationRes res = new MonthsEvaluationRes(Boolean.TRUE,CodeTypes.Success.getCodeStr(),CodeTypes.Success.getMsg(),monthslist);
        return res;
    }


    private OrderCommentInfo transfer(OrderComment entity){
        if (null == entity){
            return null;
        }
        OrderCommentInfo model = new OrderCommentInfo();
        model.setOrderCommentId(entity.getOrderCommentId());
        model.setOrderId(entity.getServiceOrderId());
        model.setUserId(entity.getUserId());
        model.setProductTypeId(entity.getProductTypeId());
        model.setCarTypeId(entity.getCarTypeId());
        model.setCity(entity.getCity());
        model.setDriverId(entity.getDriverId());
        model.setScore(entity.getScore());
        model.setContent(entity.getContent());
        model.setEndTimeStamp(entity.getEndTime());
        model.setEvaluation(entity.getEvaluation());
        model.setCreateTimeStamp((long)entity.getCreateTime());
        model.setStatus((int)entity.getStatus());
        return model;
    }

    /*
     *当缓存中无法查询到司机好评率时
     * 从数据库查询
     */
    private EvaluationRateRes getEvalFromDB(EvaluationRateReq evaluationRateReq){
        logger.info("getEvalFromDB|数据库查询好评率:"+evaluationRateReq);
        FeedActivityEvaluationInfo query = new FeedActivityEvaluationInfo();
        query.setDriverId(evaluationRateReq.getDriverId());
        query.setActivityId(evaluationRateReq.getActivityId());
        FeedActivityEvaluationInfo dbInfo = feedActivityEvaluationMangoDao.get(query);
        if(null == dbInfo){
            logger.warn("getEvalFromDB|数据库中未查询到记录|"+evaluationRateReq);
            return null;
        }
        return new EvaluationRateRes(Boolean.TRUE,"0","成功",dbInfo.getLastOrderId(),dbInfo.getEvaluation());
    }

    /*
     *发送好评率消息系统 MQ
     */
    private void sendMQ(AddCommentReq addCommentReq, ValidateTypes val){
        EvaluationInfo evaluationInfo = new EvaluationInfo();
        evaluationInfo.setDriverId(addCommentReq.getOrderInfo().getDriverId());
        evaluationInfo.setEvaluation(val.getTypeCode());
        long endTime = addCommentReq.getOrderInfo().getEndTime();
        evaluationInfo.setEndTime(endTime);
        evaluationInfo.setOrderId(addCommentReq.getOrderId());
        logger.info("sendMQ|发送评价通知|START|"+evaluationInfo);
        try {
            amqpTemplateComment.convertAndSend(evaluationInfo);
            logger.info("sendMQ|发送评价通知|END|"+evaluationInfo);
        } catch (AmqpException e) {
            logger.error("sendMQ|发送评价通知错误|"+addCommentReq,e);
            throw e;
        }
    }

    /*
    *发送司机端MQ
    * 不抛出异常
    */
    private void sendDriverMQ(AddCommentReq addCommentReq,ValidateTypes val){
        OrderInfo orderInfo = addCommentReq.getOrderInfo();
        DriverMQInfo mqInfo = new DriverMQInfo();
        mqInfo.setService_order_id(addCommentReq.getOrderId());
        mqInfo.setDriver_id(orderInfo.getDriverId());
        mqInfo.setEvaluation(val.getTypeCode());
        //时间改为插入记录时间
        Date now = new Date();
        mqInfo.setTime(DateUtil.getNumber(now));
        logger.info("sendDriverMQ|发送司机端MQ|"+mqInfo);
        PHPSerializerUtil phpSerializer = new PHPSerializerUtil();
        try {
            amqpTemplateDriver.convertAndSend(phpSerializer.serialize(mqInfo.getMap()));
            logger.info("sendDriverMQ|发送司机端MQ|END|"+addCommentReq.getOrderId());
        } catch (Exception e) {
            logger.error("sendDriverMQ|发送司机端MQ失败|"+mqInfo,e);
        }
    }


    /*
    *添加comment记录
    */
    @Transactional(rollbackFor =Exception.class)
    private void addCommentRecord(AddCommentReq addCommentReq, ValidateTypes val) throws Exception{
        OrderInfo orderInfo = addCommentReq.getOrderInfo();
        OrderComment orderComment = new OrderComment();
        orderComment.setCommentUserId(addCommentReq.getUserId());
        orderComment.setServiceOrderId(addCommentReq.getOrderId());
        orderComment.setUserId(addCommentReq.getUserId());
        orderComment.setProductTypeId(orderInfo.getProductTypeId());
        orderComment.setCarTypeId(orderInfo.getCarTypeId());
        orderComment.setCity(orderInfo.getCity());
        orderComment.setDriverId(orderInfo.getDriverId());
        orderComment.setScore((int)addCommentReq.getScore());
        orderComment.setContent(addCommentReq.getContent());
        if(StringUtils.isEmpty(orderComment.getContent())){
            orderComment.setContent("");
        }

        Date now = new Date();

        orderComment.setCreateTime(DateUtil.getNumber(now));
        orderComment.setUpdateTime(DateUtil.getNumber(now));
        //for gc
        now = null;

        int endTime = orderInfo.getEndTime();
        orderComment.setOperatorId(0);
        orderComment.setEndTime(endTime);
        orderComment.setStatus((byte)1);

        orderComment.setDisplayStatus(new Byte("1"));
        //score1-5已经废弃
        orderComment.setScore1(0);
        orderComment.setScore2(0);
        orderComment.setScore3(0);
        orderComment.setScore4(0);
        orderComment.setScore5(0);
        orderComment.setEvaluation(val.getTypeCode());
        logger.info("addCommentRecord|新增评价记录|"+orderComment);
        try {
            orderCommentMapper.insert(orderComment);
        } catch (Exception e) {
            logger.error("addCommentRecord|持久化评价错误|"+orderComment,e);
            throw e;
        }
        List<Integer> commentTagIds = addCommentReq.getCommentTagIds();
        //发送数据到网约车监管平台
        //后期方案改造为写到本地文件，然后由运维向kafka里写入数据
        sendCommentToMonitor_new(orderComment,commentTagIds);

    }


    /*
    *更新订单临时表状态
    */
    @Transactional(rollbackFor =Exception.class)
    private boolean updateTemp(AddCommentReq addCommentReq, ValidateTypes val){
        //正常情况  订单MQ先到，然后才能评论
        CompletedOrder record = new CompletedOrder();
        record.setServiceOrderId(addCommentReq.getOrderId());
        record.setDriverId((long)addCommentReq.getOrderInfo().getDriverId());
        record.setEvaluation((int)val.getTypeCode());
        record.setStatus(TempOrderStatus.UserComment.getCode());
        record.setUpdateDate(new Date());
        record.setRemark("User Judged");
        long res = 0;
        try {
            res= completedOrderMapper.updateStatus(record);
            logger.info("updateTemp|更新临时表ok|"+res);
        } catch (Exception e) {
            logger.error("updateTemp|更新临时表状态错误|"+record,e);
        }
        if(0 == res){
            record.setUserId((long)addCommentReq.getUserId());
            record.setEndTime((long)addCommentReq.getOrderInfo().getEndTime());
            record.setCreateDate(new Date());
            record.setRemark("warn: User Comment Come Early");
            //考虑MQ延迟，评论请求先到的情况
            try {
                res = completedOrderMapper.insert(record);
                logger.info("updateTemp|尝试新增记录ok|"+res);
            }catch (Exception e) {
                logger.warn("updateTemp|尝试新增记录失败|"+record,e);
            }
        }
        return res > 0;
    }


	@Override
	public List<OrderCommentInfo> getCommentListByIds(String orderIds,
			int tagFlag) {
		
		String[] ids = orderIds.split(",");
		OrderComment entity = null;
		List<OrderCommentInfo> res = new ArrayList<OrderCommentInfo>();
		List<CommentTagModel> resTags = null;
		for (String orderId : ids) {
            resTags = new ArrayList<CommentTagModel>();
            entity = orderCommentMapper.selectByOrderId(Long.valueOf(orderId));
	        logger.info("getCommentListByIds|查询评价:"+entity.toString());
	        OrderCommentInfo model = transfer(entity);
	        List<CommentTagModel> tags = tagService.getOrderTags(model.getOrderId());
            if (tags != null && tags.size() > 0) {
                if (tagFlag == -1) {
                    for (CommentTagModel tag : tags) {
                        if (tag.getType() == -1) {
                            resTags.add(tag);
                        }
                    }
                } else if (tagFlag == 1) {
                    for (CommentTagModel tag : tags) {
                        if (tag.getType() == 1) {
                            resTags.add(tag);
                        }
                    }
                } else {
                    for (CommentTagModel tag : tags) {
                        resTags.add(tag);
                    }
                }
            }
	        model.setTags(resTags);
	        res.add(model);
		}
		 logger.info("getCommentListByIds|查询评价List:"+res.toString());
		return res;
	}


    @Override
    public void changeDisplayStatus(long orderId, int displayStatus, int operatorId) throws FeedException {
        if (null == getByOrderId(orderId)){
            logger.warn("changeDisplayStatus|未查询到记录|"+orderId);
            throw new FeedException(CodeTypes.NoRecord.getCode(),CodeTypes.NoRecord.getMsg());
        }
        OrderComment record = new OrderComment();
        record.setOperatorId(operatorId);
        record.setServiceOrderId(orderId);
        if (displayStatus == 1){
            record.setDisplayStatus((byte)1);
        }else{
            record.setDisplayStatus((byte)0);
        }
        record.setUpdateTime(DateUtil.getNumber(new Date()));
        logger.info("changeStatus|更新评价displayStatus状态|"+record);
        try {
            orderCommentMapper.changeDisplayStatus(record);
            logger.info("changeStatus|更新评价displayStatus状态OK|"+orderId);
        } catch (Exception e) {
            logger.error("changeStatus|更新评价displayStatus状态错误|"+orderId,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }
    }

    @Override
    public int getDriverAvgScore(Long driverId) throws FeedException{
        int driverAvgScore=0;
        try {
            driverAvgScore = (int)orderCommentMapper.getDriverAvgScore(driverId);
            logger.info("getDriverAvgScore|查询获得司机的平均分|driverId:"+driverId+"|AVG:|"+driverAvgScore);
        } catch (Exception e) {
            logger.error("getDriverAvgScore|查询获得司机的平均分错误|driverId:|"+driverId,e);
            throw new FeedException(CodeTypes.SystemError.getCode(),CodeTypes.SystemError.getMsg());
        }
        return driverAvgScore;
    }

}
