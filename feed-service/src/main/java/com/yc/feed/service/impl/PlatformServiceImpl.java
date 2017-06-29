package com.yc.feed.service.impl;

import com.google.gson.Gson;
import com.le.config.client.ConfigManager;
import com.yc.feed.api.util.DateUtil;
import com.yc.feed.api.util.PSFClient;
import com.yc.feed.domain.config.PsfConfig;
import com.yc.feed.domain.config.TestConfig;
import com.yc.feed.domain.enums.CodeTypes;
import com.yc.feed.domain.model.PhpCommonRes;
import com.yc.feed.service.PlatformService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yusong on 2016/10/20.
 * 与平台交互接口实现
 */
@Service
public class PlatformServiceImpl implements PlatformService , InitializingBean {

    private final Logger logger = Logger.getLogger(PlatformServiceImpl.class);

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String SUMMARY_TEXT = "您有一条新消息";

    //@Resource
    private PsfConfig psfConfig;

    //调用客户端
    private PSFClient psfClient = null;

    @Override
    public boolean updateOrder(long orderNo,boolean commentOk) {
        PSFClient.PSFRPCRequestData request = new PSFClient.PSFRPCRequestData();
        String para = URLEncoder.encode("flag");
        para += "=";
        para += URLEncoder.encode("1");
        para += "&";
        para += URLEncoder.encode("set");
        para += "=";
        if(commentOk){
            para += URLEncoder.encode("1");
        }else{
            para += URLEncoder.encode("0");
        }
        para += "&";
        para += URLEncoder.encode("where");
        para += "=";
        para += "service_order_id="+orderNo;
        request.data = para;
        request.service_uri= psfConfig.getUpdateOrderPath();
        String response = null;
        logger.info("updateOrder|更新平台订单请求:"+request.data);
        try {
            response = psfClient.call("order", request, 30);
            logger.info("updateOrder|更新平台订单响应:"+response);
        } catch (Exception e) {
            logger.error("updateOrder|更新平台订单错误|",e);
            return Boolean.FALSE;
        }
        return parseRes(response);
    }



    /*
    $trackParams = array(
                'order_id'    => $orderId,
                'action_name' => '评价',
                'username'    => $operatorId?$operatorId:$userName,
                'dateline'    => time()
            );
     */
    @Override
    public boolean appendTrack(long orderId, int userId, Date dateline) {
        PSFClient.PSFRPCRequestData request = new PSFClient.PSFRPCRequestData();
        request.service_uri= psfConfig.getAppendTrackPath();
        String para = URLEncoder.encode("order_id");
        para += "=";
        para += URLEncoder.encode(String.valueOf(orderId));
        para += "&";
        para += URLEncoder.encode("action_name");
        para += "=";
        para += URLEncoder.encode("评价");
        para += "&";
        para += URLEncoder.encode("username");
        para += "=";
        para += URLEncoder.encode(String.valueOf(userId));
      /*  para += "&";
        para += URLEncoder.encode("dateline");
        para += "=";
        para += URLEncoder.encode(sdf.format(dateline));*/
        request.data = para;
        String response = null;
        logger.info("appendTrack|追加评价轨迹请求:"+request.data);
        try {
            response = psfClient.call("order", request, 30);
            logger.info("appendTrack|追加评价轨迹响应:"+response);
        } catch (Exception e) {
            logger.error("appendTrack|追加评价轨迹错误|",e);
            return Boolean.FALSE;
        }
        return parseRes(response);
    }




    @Override
    public boolean sendSMS(long driverId,String content) {
        PSFClient.PSFRPCRequestData request = new PSFClient.PSFRPCRequestData();
        Gson gson = new Gson();
        Map<String,Object> para = new HashMap<String, Object>();
        List<String> userIdsList = new ArrayList<String>();
        userIdsList.add(String.valueOf(driverId));
        Map<String,Object> userMap = new HashMap<String, Object>();
        userMap.put("userIds",userIdsList);
        para.put("users",userMap);
        para.put("userType","DR");
        para.put("msgType",22000);
        para.put("summary",SUMMARY_TEXT);
        Map<String,Object> contentMap = new HashMap<String, Object>();
        contentMap.put("method","msg");
        Map<String,Object> paraMap = new HashMap<String, Object>();
        paraMap.put("pushtime",DateUtil.getNumber(new Date()));
        paraMap.put("type","comment");
        paraMap.put("msg",content);
        contentMap.put("params",paraMap);
        para.put("content",gson.toJson(contentMap));
        para.put("ttl",120);
        para.put("additional",new ArrayList<Object>());
        para.put("source",1);
        para.put("tag",null);
        para.put("sendTime",0);
        para.put("flags",0);

        request.data = gson.toJson(para);
        request.service_uri="/message/pushMultiNotification";
        String response = null;
        logger.info("sendSMS|发送短信请求:"+request.data);
        try {
            response = psfClient.call("message", request, 30);
            logger.info("sendSMS|发送短信响应:"+response);
        } catch (Exception e) {
            logger.error("sendSMS|发送短信错误|",e);
            return Boolean.FALSE;
        }
        return parseRes(response);
    }

    @Override
    public int getDriverServiceCount(long driverId, long userId) {
        long startTime = System.currentTimeMillis();
        PSFClient.PSFRPCRequestData request = new PSFClient.PSFRPCRequestData();
        String params = "driver_id="+driverId+"&user_id="+userId+"&force_master=0";
        request.data = params;
        request.service_uri = psfConfig.getServiceCountPath();
        String response = null;
        logger.info("getDriverServiceCount|request|:"+request.data);
        int serviceTimes=0;
        try {
            response = psfClient.call("order", request, 30);
            logger.info("getDriverServiceCount|"+(System.currentTimeMillis()-startTime)+"ms|response:"+response);
        } catch (Exception e) {
            logger.error("getDriverServiceCount|获得服务次数错误|",e);
            return 0;
        }
        Gson gson = new Gson();
        if(response!= null){
            Map<String,String> resultMap = gson.fromJson(response,Map.class);
            if(resultMap.get("ret_msg") !=null && "success".equals(resultMap.get("ret_msg"))){
                serviceTimes = Integer.parseInt(resultMap.get("result"));
            }
        }
        return serviceTimes;
    }

    /*
     *处理平台结果
     */
    private boolean parseRes(String response){
        Gson gson = new Gson();
        PhpCommonRes res = gson.fromJson(response,PhpCommonRes.class);
        if(null != res && CodeTypes.Success.getCode() == res.getRet_code()){
            return Boolean.TRUE;
        }else{
            return Boolean.FALSE;
        }
    }



    /*
    *初始化客户端
    */
/*    private void initClient(){
        logger.info("initClient|初始化平台调用客户端|START|config："+platformConfig);
        String[] serviceCenter = {platformConfig.getHost1(), platformConfig.getHost2()};
        try {
            psfClient = new PSFClient(serviceCenter, 30, 30, 4096, 3);
        } catch (Exception e) {
            logger.error("initClient|初始化失败.",e);
        }
        logger.info("initClient|初始化平台调用客户端|END|");
    }*/

    /*
    *从配置中心获取配置初始化客户端
    */
    private void initClientFromConfigCenter(){
        try {
            psfConfig = ConfigManager.get(PsfConfig.class);
            logger.info("initClient|初始化平台调用客户端|START|config："+psfConfig);
            String[] serviceCenter = {psfConfig.getHost1(), psfConfig.getHost2()};
            try {
                psfClient = new PSFClient(serviceCenter, 30, 30, 4096, 3);
            } catch (Exception e) {
                logger.error("initClient|读取配置中心错误.",e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("initClient|初始化平台调用客户端|END|");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        initClientFromConfigCenter();
    }
}
