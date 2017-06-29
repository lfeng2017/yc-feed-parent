package com.yc.feed.bean;

import com.le.config.client.ConfigManager;
import com.yc.feed.domain.config.JdbcDataSourceConfig;
import com.yongche.config.database.DataSourceInstance;
import com.yongche.config.database.Database;
import com.yongche.config.database.DatabaseConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author lfeng
 * @version 下午4:44
 * @since 2017/1/18
 */
public class ConfigCenterDataSource implements BeanDefinitionRegistryPostProcessor {

    String SLAVE_NAME = "%s_slave_%s";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        JdbcDataSourceConfig databaseConfig = ConfigManager.get(JdbcDataSourceConfig.class);
        for (Database database : databaseConfig.getDatabases()) {
            registerDataSourceBeanDefinition(database, registry);
        }
    }

    protected void registerDataSourceBeanDefinition(Database database,BeanDefinitionRegistry registry) {
        DataSourceInstance master = database.getMaster();
        List<DataSourceInstance> slaves = database.getSlaves();
        registry.registerBeanDefinition(database.getName(), buildDataSourceBeanDefinition(master, false));
        if (slaves != null && (!slaves.isEmpty())) {
            for (int i = 0, size = slaves.size(); i < size; i++) {
                registry.registerBeanDefinition(String.format(SLAVE_NAME, database.getName(), i), buildDataSourceBeanDefinition(slaves.get(i), true));
            }
        }
    }

    Class<?> dataSourceClass = HikariDataSource.class;
    String driverClassName = "com.mysql.jdbc.Driver";
    protected RootBeanDefinition buildDataSourceBeanDefinition(DataSourceInstance dsi, boolean readOnly) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(dataSourceClass);
        int port = dsi.getPort();
        if (port == 0) {
            port = 3306;
        }
        String url = String.format("jdbc:mysql://%s:%s/%s", dsi.getHost(), port, dsi.getDb());
        int maximumPoolSize = dsi.getMaximumPoolSize();
        if (maximumPoolSize < 10) {
            maximumPoolSize = 10;
        }
        if (maximumPoolSize > 1000) {
            maximumPoolSize = 1000;
        }
        int connectionTimeout = Math.max(dsi.getConnectionTimeout(), 300);

        beanDefinition.getPropertyValues().add("driverClassName", driverClassName);
        beanDefinition.getPropertyValues().add("jdbcUrl", url);
        beanDefinition.getPropertyValues().add("username", dsi.getUsername());
        beanDefinition.getPropertyValues().add("password", dsi.getPassword());
        beanDefinition.getPropertyValues().add("maximumPoolSize", maximumPoolSize);
        beanDefinition.getPropertyValues().add("connectionTimeout", connectionTimeout);
        beanDefinition.getPropertyValues().add("autoCommit", true);
        beanDefinition.getPropertyValues().add("readOnly", readOnly);
        return beanDefinition;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
