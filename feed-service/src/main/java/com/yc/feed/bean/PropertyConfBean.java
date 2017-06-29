package com.yc.feed.bean;

import com.le.config.client.ConfigManager;
import com.yc.feed.domain.config.RabbitConnectConfig;
import com.yc.feed.domain.model.RabbitMqConfigInfo;
import com.yongche.config.rabbitmq.Exchange;
import com.yongche.config.rabbitmq.Queue;
import org.apache.log4j.Logger;
import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lfeng on 2016/12/28.
 * 该类是运用spring依赖注入的方式，直接从配置中心拿到配置后，写入到全局的property中
 */
public class PropertyConfBean extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer{

    static final Logger logger = Logger.getLogger(PropertyConfBean.class);

    public PropertyConfBean() {
        //将rabbitmq相关的配置放入property中
        RabbitConnectConfig rabbitmqConfig = ConfigManager.get(RabbitConnectConfig.class);
        List<RabbitMqConfigInfo> rabbitMqConfigInfos = rabbitmqConfig.getRabbits();
        Properties properties = new Properties();
        for(RabbitMqConfigInfo amqpConfig :rabbitMqConfigInfos){
            buildPassword(properties,amqpConfig);
            buildAddress(properties,amqpConfig);
            buildUserName(properties,amqpConfig);
            buildHost(properties,amqpConfig);
            List<Exchange> exchanges = amqpConfig.getExchanges();
            if(exchanges !=null){
                for(Exchange exchange : exchanges){
                    buildExchange(properties,exchange);
                    List<Queue> queues = exchange.getQueues();
                    for(Queue queue : queues){
                        buildQueue(properties,queue);
                        buildRouteKey(properties,queue);
                    }
                }
            }
        }
        showKeysAndValues(properties);
        setProperties(properties);
    }

    private static void showKeysAndValues(Properties properties) {
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("rabbit mq config key:{},value:{}"+key+"===="+value);
            logger.info("rabbit mq config key:{},value:{}"+key+"===="+value);
        }
    }

    @PostConstruct
    public void init(){

    }

    void buildPassword(Properties properties,RabbitMqConfigInfo amqpConfig){
        properties.setProperty((amqpConfig.getId()+".password"), amqpConfig.getPassword());
    }
    void buildAddress(Properties properties,RabbitMqConfigInfo amqpConfig){
        properties.setProperty((amqpConfig.getId()+".address"), amqpConfig.getAddress());
    }
    void buildUserName(Properties properties,RabbitMqConfigInfo amqpConfig){
        properties.setProperty((amqpConfig.getId()+".username"), amqpConfig.getUsername());
    }
    void buildHost(Properties properties,RabbitMqConfigInfo amqpConfig){
        properties.setProperty((amqpConfig.getId()+".vhost"), amqpConfig.getHost());
    }
    void buildExchange(Properties properties,Exchange exchange){
        properties.setProperty(exchange.getName(),exchange.getName());
    }
    void buildQueue(Properties properties,Queue queue){
        properties.setProperty(queue.getName(),queue.getName());
    }
    void buildRouteKey(Properties properties,Queue queue){
        properties.setProperty(queue.getRouteKey(),queue.getRouteKey());
    }
}
