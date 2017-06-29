package com.yc.feed.domain.config;

import com.yc.feed.domain.model.RabbitMqConfigInfo;
import com.yongche.config.database.Database;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by lfeng on 2016/12/26.
 */
@Root(name = "rabbit-connect")
public class RabbitConnectConfig {

    @ElementList(entry = "rabbit", inline = true)
    private List<RabbitMqConfigInfo> rabbits;

    public List<RabbitMqConfigInfo> getRabbits() {
        return rabbits;
    }

    public void setRabbits(List<RabbitMqConfigInfo> rabbits) {
        this.rabbits = rabbits;
    }
}
