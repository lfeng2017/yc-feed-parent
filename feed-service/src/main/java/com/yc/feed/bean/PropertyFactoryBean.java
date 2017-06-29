package com.yc.feed.bean;

import com.le.config.client.ConfigManager;
import com.yc.feed.domain.config.PropertyMap;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Properties;

/**
 * <p>yc-feed-parent
 * <p>com.yc.feed.test
 *
 * @author stony
 * @version 下午1:44
 * @since 2017/3/7
 */
public class PropertyFactoryBean implements FactoryBean<Properties> {
    private static final Logger logger = LoggerFactory.getLogger(PropertyFactoryBean.class);
    @Override
    public Properties getObject() throws Exception {
        PropertyMap map = ConfigManager.get(PropertyMap.class);
        Properties properties = new Properties();
        properties.putAll(map.getMap());
        logger.info("load config properties {}", map.getMap());
        return properties;
    }

    @Override
    public Class<?> getObjectType() {
        return Properties.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
