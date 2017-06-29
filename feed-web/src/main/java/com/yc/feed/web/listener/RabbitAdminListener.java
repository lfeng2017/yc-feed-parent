package com.yc.feed.web.listener;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import java.util.Map;

/**
 * Created by lfeng on 2017/1/9.
 *
 * 该类是用来测试线上的rabbitmq链接是否正常的，与业务没有关系。之后可以删除
 */
public class RabbitAdminListener implements ApplicationListener<ContextRefreshedEvent>{
    ApplicationContext applicationContext;
    static final Logger logger = Logger.getLogger(RabbitAdminListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){
            this.applicationContext = contextRefreshedEvent.getApplicationContext();
            Map<String,RabbitAdmin> rabbitAdminMap = applicationContext.getBeansOfType(RabbitAdmin.class);
            for(Map.Entry<String,RabbitAdmin> entry : rabbitAdminMap.entrySet()){
                RabbitAdmin admin = entry.getValue();
                logger.info(entry.getKey() + " ============ " + admin.getRabbitTemplate().getConnectionFactory());
                logger.info(entry.getKey() + " ============ " + admin.getRabbitTemplate().getConnectionFactory().getVirtualHost());
            }
            Map<String, CachingConnectionFactory> connectionFactoryMap = applicationContext.getBeansOfType(CachingConnectionFactory.class);
            for(Map.Entry<String,CachingConnectionFactory> entry: connectionFactoryMap.entrySet()){
                logger.info(entry.getKey() + " ============ " + entry.getValue() + " ," + entry.getValue().getVirtualHost());
            }

            Map<String, Queue> queueMap = applicationContext.getBeansOfType(Queue.class);
            for(Map.Entry<String,Queue> entry: queueMap.entrySet()){
                logger.info(entry.getKey() + " ============ " + entry.getValue());
            }

        }
    }
}
