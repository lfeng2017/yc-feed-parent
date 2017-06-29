package com.yc.feed.bean;

import com.le.config.client.ConfigManager;
import com.yc.feed.domain.config.DictConfig;
import com.yc.feed.domain.config.LogConfig;
import org.apache.log4j.Logger;
import org.springframework.util.Log4jConfigurer;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.Log4jConfigListener;
import org.springframework.web.util.ServletContextPropertyUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.FileNotFoundException;

/**
 * Created by lfeng on 2016/12/26.
 */
public class LoggerConfigBean extends Log4jConfigListener {
    //private static Logger logger = Logger.getLogger(LoggerConfigBean.class);

    public LoggerConfigBean() {
        super();
    }

    public void contextInitialized(ServletContextEvent event){
        //配置中心是懒加载，需要先把配置文件download到本地
        LogConfig logConfig = ConfigManager.get(LogConfig.class);
        if(logConfig == null){
            System.err.println("LoggerConfigBean|contextInitialized|加載log4j配置文件報錯");
            return;
        }
        initLogging(event.getServletContext());
    }

    public static void initLogging(ServletContext servletContext) {
        if(exposeWebAppRoot(servletContext)) {
            WebUtils.setWebAppRootSystemProperty(servletContext);
        }
        String logKey = servletContext.getInitParameter("log4jConfigLocation");
        DictConfig dictConfig = ConfigManager.get(DictConfig.class);
        String location = dictConfig.getDictMap().get(logKey);
        if(location != null) {
            try {
                /*location = ServletContextPropertyUtils.resolvePlaceholders(location, servletContext);
                if(!ResourceUtils.isUrl(location)) {
                    location = WebUtils.getRealPath(servletContext, location);
                }*/

                servletContext.log("Initializing log4j from [" + location + "]");
                String ex = servletContext.getInitParameter("log4jRefreshInterval");
                //此处增加一个时间，是可以自动刷新配置文件
                if(!StringUtils.hasText(ex)) {
                    ex = "10000";
                }
                if(StringUtils.hasText(ex)) {
                    try {
                        long ex1 = Long.parseLong(ex);
                        Log4jConfigurer.initLogging(location, ex1);
                    } catch (NumberFormatException var5) {
                        Log4jConfigurer.initLogging(location);
                        throw new IllegalArgumentException("Invalid \'log4jRefreshInterval\' parameter: " + var5.getMessage());
                    }
                } else {
                    Log4jConfigurer.initLogging(location);
                }
            } catch (FileNotFoundException var6) {
                throw new IllegalArgumentException("Invalid \'log4jConfigLocation\' parameter: " + var6.getMessage());
            }
        }

    }

    private static boolean exposeWebAppRoot(ServletContext servletContext) {
        String exposeWebAppRootParam = servletContext.getInitParameter("log4jExposeWebAppRoot");
        return exposeWebAppRootParam == null || Boolean.valueOf(exposeWebAppRootParam).booleanValue();
    }
}
