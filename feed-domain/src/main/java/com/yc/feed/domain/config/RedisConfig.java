package com.yc.feed.domain.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yusong on 2016/12/21.
 * redis集群配置中心配置
 */
@Root(name = "redis-cluster")
public class RedisConfig {

    @Attribute(name = "passord", required = false)
    private String passord;
    @Element(name = "master1", required = true)
    private String master1;
    @Element(name = "port1", required = true)
    private String port1;
    @Element(name = "master2", required = true)
    private String master2;
    @Element(name = "port2", required = true)
    private String port2;
    @Element(name = "master3", required = true)
    private String master3;
    @Element(name = "port3", required = true)
    private String port3;

    @Override
    public String toString() {
        return "RedisConfig{" +
                "passord='" + passord + '\'' +
                ", master1='" + master1 + '\'' +
                ", port1='" + port1 + '\'' +
                ", master2='" + master2 + '\'' +
                ", port2='" + port2 + '\'' +
                ", master3='" + master3 + '\'' +
                ", port3='" + port3 + '\'' +
                '}';
    }

    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    public String getMaster1() {
        return master1;
    }

    public void setMaster1(String master1) {
        this.master1 = master1;
    }

    public String getPort1() {
        return port1;
    }

    public void setPort1(String port1) {
        this.port1 = port1;
    }

    public String getMaster2() {
        return master2;
    }

    public void setMaster2(String master2) {
        this.master2 = master2;
    }

    public String getPort2() {
        return port2;
    }

    public void setPort2(String port2) {
        this.port2 = port2;
    }

    public String getMaster3() {
        return master3;
    }

    public void setMaster3(String master3) {
        this.master3 = master3;
    }

    public String getPort3() {
        return port3;
    }

    public void setPort3(String port3) {
        this.port3 = port3;
    }
}
