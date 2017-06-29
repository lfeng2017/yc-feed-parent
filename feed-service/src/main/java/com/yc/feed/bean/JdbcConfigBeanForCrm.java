package com.yc.feed.bean;

import com.le.config.client.ConfigManager;
import com.yc.feed.domain.config.JdbcDataSourceConfig;
import com.yongche.config.database.DataSourceInstance;
import com.yongche.config.database.Database;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jfaster.mango.datasource.DriverManagerDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by lfeng on 2016/12/22.
 * jdbc集群spring配置
 * 该类已经被ConfigCenterDataSource替代
 */
public class JdbcConfigBeanForCrm extends AbstractDataSource implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(JdbcConfigBeanForCrm.class);

    DataSource delegate;
    @Override
    public Connection getConnection() throws SQLException {
        return delegate.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return delegate.getConnection(username,password);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        initFromCenter();
    }

    public void initFromCenter(){
        try {
            JdbcDataSourceConfig databaseConfig = ConfigManager.get(JdbcDataSourceConfig.class);
            List<Database> databases = databaseConfig.getDatabases();
            DataSourceInstance master = databases.get(1).getMaster();
            delegate = buildDataSource(master);
        } catch (Exception e) {
            logger.error("JdbcConfigBeanForCrm|initFromCenter|从配置中心加载jdbc配置错误|",e);
        }
    }

    protected static DataSource buildDataSource(DataSourceInstance dsi) {
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


        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(url);
        config.setUsername(dsi.getUsername());
        config.setPassword(dsi.getPassword());
        config.setMaximumPoolSize(maximumPoolSize);
        config.setConnectionTimeout(connectionTimeout);
        config.setAutoCommit(true);
        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

}
