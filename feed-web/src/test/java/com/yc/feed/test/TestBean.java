package com.yc.feed.test;

/**
 * <p>yc-feed-parent
 * <p>com.yc.feed.test
 *
 * @author stony
 * @version 上午11:48
 * @since 2017/3/7
 */
public class TestBean {
    String addresses;
    String username;
    String password;
    String virtualHost;

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "addresses='" + addresses + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", virtualHost='" + virtualHost + '\'' +
                '}';
    }
}
