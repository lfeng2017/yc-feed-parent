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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

/**
 * Created by lfeng on 2016/12/22.
 * jdbc集群spring配置
 * 该类已经被ConfigCenterDataSource替代
 */
public class JdbcConfigBean extends DriverManagerDataSource{
    private final Logger logger = LoggerFactory.getLogger(JdbcConfigBean.class);

    @PostConstruct
    public void initFromCenter(){
        try {
            JdbcDataSourceConfig databaseConfig = ConfigManager.get(JdbcDataSourceConfig.class);
            List<Database> databases = databaseConfig.getDatabases();
            DataSourceInstance master = databases.get(0).getMaster();
            super.setDriverClassName("org.gjt.mm.mysql.Driver");
            super.setUsername(master.getUsername());
            super.setPassword(master.getPassword());
            String url ="jdbc:mysql://"+master.getHost()+":"+master.getPort()+"/"+master.getDb()+"?useUnicode=true&amp;characterEncoding=utf8";
            System.out.println("=================="+url);
            super.setUrl(url);
        } catch (Exception e) {
            logger.error("JdbcConfigBean|从配置中心加载jdbc配置错误|",e);
        }
    }

}
