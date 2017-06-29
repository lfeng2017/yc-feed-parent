package com.yc.feed.service.impl;

import com.le.config.client.ConfigManager;
import com.yc.feed.domain.config.TestConfig;
import com.yc.feed.service.ConfigureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * Created by yusong on 2016/10/10.
 * 从配置中心加载配置服务
@Service
 */
public class ConfigureServiceImpl implements ConfigureService, ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(ConfigureServiceImpl.class);











    /*
    *容器加载后执行
    */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(null != event.getApplicationContext().getParent()){
            loadConfig();
        }
    }

    /*
    *获取配置
    * 此方法不抛出异常
    */
    private void loadConfig(){
        try {
            TestConfig testConfig = ConfigManager.get(TestConfig.class);
            logger.info("loadConfig|加载配置中心配置成功",testConfig);
            //
        } catch (Exception e) {
            logger.error("loadConfig|加载配置中心配置出错",e);
        }
    }


}
