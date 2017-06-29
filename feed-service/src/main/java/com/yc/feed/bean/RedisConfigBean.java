package com.yc.feed.bean;

import com.le.config.client.ConfigManager;
import com.yc.feed.domain.config.RedisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yusong on 2016/12/21.
 * redis集群spring配置
 */
public class RedisConfigBean  {
    private final Logger logger = LoggerFactory.getLogger(RedisConfigBean.class);

    private RedisConfig redisConfig;

    public void initFromCenter(){
        try {
            redisConfig = ConfigManager.get(RedisConfig.class);
            logger.info("RedisConfigBean|从配置中心加载redis配置成功|"+redisConfig);
        } catch (Exception e) {
            logger.error("RedisConfigBean|读取配置中心错误|从配置中心加载redis配置错误|",e);
        }
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }
}
