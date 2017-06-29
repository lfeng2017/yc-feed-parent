package com.yc.feed.test;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;
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

    @Override
    public Properties getObject() throws Exception {
        Serializer serializer = new Persister();
        ClassPathResource resource = new ClassPathResource("props/test/rabbit.xml");
        File source = resource.getFile();
        PropertyMap map = serializer.read(PropertyMap.class, source);
        Properties properties = new Properties();
        properties.putAll(map.getMap());
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
