package com.yc.feed.test;

import com.le.config.client.ConfigManager;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

/**
 * <p>yc-feed-parent
 * <p>com.yc.feed.test
 *
 * @author stony
 * @version 下午1:45
 * @since 2017/3/7
 */
public class PropertyMapTest {

    static {
        String fileName = PropertyMapTest.class.getResource("/center/config-node-info-test.xml").getPath();
        System.setProperty(ConfigManager.ENV_CONFIG_INFO_FILE_PATH, fileName);
    }
    @Test
    public void test_20() throws Exception {
        Serializer serializer = new Persister();
        ClassPathResource resource = new ClassPathResource("props/test/rabbit.xml");
        File source = resource.getFile();
        PropertyMap map = serializer.read(PropertyMap.class, source);
        System.out.println(map.getMap());
    }

    @Test
    public void test_25(){
        ClassPathXmlApplicationContext cxt = new ClassPathXmlApplicationContext("classpath:spring/spring-test-context.xml");
        TestBean testBean = cxt.getBean(TestBean.class);
        System.out.println(testBean);
    }

    @Test
    public void test_38(){
        ClassPathXmlApplicationContext cxt = new ClassPathXmlApplicationContext("classpath:spring/spring-test-context.xml");

        RabbitAdmin rabbitAdmin = (RabbitAdmin) cxt.getBean("rabbitAdmin");
        System.out.println(rabbitAdmin);

        rabbitAdmin.deleteQueue("test_xxx");
    }

    @Test
    public void test_49(){
        ClassPathXmlApplicationContext cxt = new ClassPathXmlApplicationContext("classpath:conf/spring-config.xml");


        RabbitTemplate amqpTemplateComment = (RabbitTemplate) cxt.getBean("amqpTemplateComment");

        System.out.println(amqpTemplateComment);

    }

}
