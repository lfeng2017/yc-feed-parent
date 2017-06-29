package com.yc.feed.domain.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yusong on 2016/12/21.
 * 平台接口配置
 */
@Root(name = "push-message")
public class PsfConfig {
    //服务地址1
    @Element(name = "host1", required = true)
    private String host1;
    //服务地址2
    @Element(name = "host2", required = true)
    private String host2;
    //更新订单服务路径
    @Element(name = "updateOrderPath", required = true)
    private String updateOrderPath;
    //新增评价轨迹路径
    @Element(name = "appendTrackPath", required = true)
    private String appendTrackPath;
    //查询服务次数
    @Element(name = "serviceCountPath", required = true)
    private String serviceCountPath;

    @Override
    public String toString() {
        return "PlatformConfig{" +
                "host1='" + host1 + '\'' +
                ", host2='" + host2 + '\'' +
                ", updateOrderPath='" + updateOrderPath + '\'' +
                ", appendTrackPath='" + appendTrackPath + '\'' +
                ", serviceCountPath='" + serviceCountPath + '\'' +
                '}';
    }

    public String getHost1() {
        return host1;
    }

    public void setHost1(String host1) {
        this.host1 = host1;
    }

    public String getHost2() {
        return host2;
    }

    public void setHost2(String host2) {
        this.host2 = host2;
    }

    public String getUpdateOrderPath() {
        return updateOrderPath;
    }

    public void setUpdateOrderPath(String updateOrderPath) {
        this.updateOrderPath = updateOrderPath;
    }

    public String getAppendTrackPath() {
        return appendTrackPath;
    }

    public void setAppendTrackPath(String appendTrackPath) {
        this.appendTrackPath = appendTrackPath;
    }

    public String getServiceCountPath() {
        return serviceCountPath;
    }

    public void setServiceCountPath(String serviceCountPath) {
        this.serviceCountPath = serviceCountPath;
    }
}
