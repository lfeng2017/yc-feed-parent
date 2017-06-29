package com.yc.feed.domain.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yongche.config.database.Database;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Administrator on 2016/12/23.
 */
@Root(name = "jdbcDataSource")
public class JdbcDataSourceConfig {
    @ElementList(entry = "database", inline = true)
    private List<Database> databases;

    public List<Database> getDatabases() {
        return databases;
    }

    public void setDatabases(List<Database> databases) {
        this.databases = databases;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.PrettyFormat,SerializerFeature.DisableCircularReferenceDetect);

    }
}
