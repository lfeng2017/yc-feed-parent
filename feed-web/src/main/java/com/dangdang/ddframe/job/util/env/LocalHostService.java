package com.dangdang.ddframe.job.util.env;

import org.apache.log4j.Logger;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by lfeng on 2017/3/30.
 * 该类是重写了getIp方法
 * 原生的方法返回的是服务器的虚ip，修改后需要返回的是实际ip(bound0)
 */
public class LocalHostService
{
    private final Logger logger = Logger.getLogger("com.yc.feed.util.LocalHostService");
    //private final Logger logger = Logger.getLogger(LocalHostService.class);
    private static volatile String cachedIpAddress;

    public LocalHostService() {
    }

    public static List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            String portName;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                portName = networkInterface.getName();
                if(!portName.contains("lo")){
                    while (inetAddresses.hasMoreElements()) {
                        inetAddress = inetAddresses.nextElement();
                        if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                            ip = inetAddress.getHostAddress();
                            ipList.add(ip);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }
    public String getIp() {
    if(null != cachedIpAddress) {
        logger.info("======elastic-job-1返回的IP======="+cachedIpAddress);
        return cachedIpAddress;
    }
        List<String> ipList = getLocalIPList();
        if(ipList != null && !ipList.isEmpty()){
            String ip = ipList.get(0);
            cachedIpAddress = ip;
            logger.info("======elastic-job-2返回的IP======="+cachedIpAddress);
            return ip;
        }
        if(null != cachedIpAddress) {
            return cachedIpAddress;
        } else {
            Enumeration netInterfaces;
            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException var7) {
                throw new HostException(var7);
            }

            String localIpAddress = null;

            while(netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface)netInterfaces.nextElement();
                Enumeration ipAddresses = netInterface.getInetAddresses();

                while(ipAddresses.hasMoreElements()) {
                    InetAddress ipAddress = (InetAddress)ipAddresses.nextElement();
                    if(this.isPublicIpAddress(ipAddress)) {
                        String publicIpAddress = ipAddress.getHostAddress();
                        cachedIpAddress = publicIpAddress;
                        logger.info("======elastic-job-3返回的IP======="+cachedIpAddress);
                        return publicIpAddress;
                    }

                    if(this.isLocalIpAddress(ipAddress)) {
                        localIpAddress = ipAddress.getHostAddress();
                    }
                }
            }

            cachedIpAddress = localIpAddress;
            logger.info("======elastic-job-4返回的IP======="+cachedIpAddress);
            return localIpAddress;
        }
    }

    private boolean isPublicIpAddress(InetAddress ipAddress) {
        return !ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !this.isV6IpAddress(ipAddress);
    }

    private boolean isLocalIpAddress(InetAddress ipAddress) {
        return ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !this.isV6IpAddress(ipAddress);
    }

    private boolean isV6IpAddress(InetAddress ipAddress) {
        return ipAddress.getHostAddress().contains(":");
    }

    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var2) {
            throw new HostException(var2);
        }
    }
}
