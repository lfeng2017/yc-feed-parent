package com.yc.feed.domain.model;

import com.yongche.config.rabbitmq.Exchange;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Created by lfeng on 2016/12/26.
 */
public class RabbitMqConfigInfo {
    @Attribute(required = true)
    private String id;
    @Attribute(required = true)
    private String address;
    @Attribute(required = true)
    private String username;
    @Attribute(required = true)
    private String host;
    @Attribute(required = true)
    private String password;
    @ElementList(entry = "exchange", inline = true, required = false)
    private List<Exchange> exchanges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Exchange> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<Exchange> exchanges) {
        this.exchanges = exchanges;
    }
}
