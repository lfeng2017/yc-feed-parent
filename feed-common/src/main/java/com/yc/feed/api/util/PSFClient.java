package com.yc.feed.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

/**
 * Created by agile6v on 8/17/16.
 * PHP接口调用工具类
 */
public class PSFClient {

    public static final String PSF_DEFAULT_CHARSET              = "UTF-8";

    public static final int PSF_MAX_SERVICE_TYPE_SIZE           = 16;
    public static final int SERVICE_CENTER_HOST_NUMBER_MAX      = 8;
    public static final int PSF_PROTO_MAGIC_NUMBER              = 0x23232323;

    public static final int PSF_PROTO_FID_ALLOC_SERVER          = 69;
    public static final int PSF_PROTO_FID_CLIENT_JOIN           = 71;
    public static final int PSF_PROTO_FID_RPC_REQ_NO_HEADER     = 75;
    public static final int PSF_PROTO_FID_RPC_REQ_WITH_HEADER   = 81;

    public static class PSFRPCRequestData 
    {
        public String service_uri;
        public String data;
        public Hashtable headers;
    }

    public class PSFProtoHeader
    {
        public int magic_number;
        public byte func_id;
        public byte status;
        public int body_len;
    }

    public class ConnectionInfo
    {
        Socket sock;
        int port;
        String ip_addr;
    }

    public class PSFClientConnectionInfo
    {
        boolean reallocate;
        boolean forbidden_alloc_server;
        ConnectionInfo conn;
    }

    public class PSFClientContext
    {
        public int connect_timeout;
        public int network_timeout;
        public int reconnect_times;
        public int service_center_host_number;
        public int last_connect_host_id;
        public int max_pkg_size;
        public byte[] send_recv_buf;
        public HashMap server_manager_hash;
        public InetSocketAddress[] service_center;
        public SFServerInfo[] service_center_host;
    }

    public class SFServerInfo
    {
        public String ip_addr;
        public int port;

        public SFServerInfo(String ip_addr, int port) {
            this.ip_addr = ip_addr;
            this.port = port;
        }
    }

    private PSFClientContext m_context;

    public String call(String serviceType, PSFRPCRequestData request,
                          int timeout) throws Exception
    {
        boolean restore = false;
        int default_network_timeout;
        PSFClientConnectionInfo clientInfo;
        String response = new String();

        if (m_context == null) {
            throw new Exception("context may not have been initialized.");
        }

        if (serviceType.length() > PSF_MAX_SERVICE_TYPE_SIZE) {
            throw new Exception("serivce_type size is greater than 16.");
        }

        if ((10 + 2 + serviceType.length() + request.data.length()) > m_context.max_pkg_size) {
            throw new Exception("the packet size exceeds max_pkg_size.");
        }

        default_network_timeout = m_context.network_timeout;
        if (default_network_timeout != timeout && timeout > 0) {
            m_context.network_timeout = timeout;
            restore = true;
        }

        try {
            clientInfo = getServerManagerConn(m_context, serviceType);
            response = executeRpcCall(clientInfo, request, serviceType);
        } catch (Exception e) {
            throw e;
        }

        if (restore) {
            m_context.network_timeout = default_network_timeout;
        }

        return response;
    }

    public void close() throws IOException {
        Set setOfKeys = m_context.server_manager_hash.keySet();
        Iterator iterator = setOfKeys.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            PSFClientConnectionInfo managerConn = (PSFClientConnectionInfo)
                    m_context.server_manager_hash.get(key);
            if (managerConn.conn.sock != null) {
                managerConn.conn.sock.close();
                managerConn.conn.sock = null;
            }
        }
    }

    public PSFClient(String[] serviceCenter, int network_timeout,
                       int connect_timeout, int max_pkg_size, int reconnect_times) throws Exception
    {
        String[] server;

        if (serviceCenter.length < 1 || serviceCenter.length > SERVICE_CENTER_HOST_NUMBER_MAX) {
            throw new Exception("the \"server_center\" is invalid, must be greater than 1 and less than 8.");
        }

        m_context = new PSFClientContext();
        m_context.connect_timeout = connect_timeout;
        m_context.network_timeout = network_timeout;
        m_context.reconnect_times = reconnect_times;
        m_context.max_pkg_size = max_pkg_size;
        m_context.service_center_host_number = serviceCenter.length;
        m_context.last_connect_host_id = 0;
        m_context.server_manager_hash = new HashMap();
        m_context.service_center_host = new SFServerInfo[serviceCenter.length];

        m_context.service_center = new InetSocketAddress[serviceCenter.length];
        for (int i = 0; i < serviceCenter.length; i++) {
            server = serviceCenter[i].split("\\:", 2);
            if (server.length != 2) {
                throw new Exception("\"serviceCenter\" is invalid, the correct format is host:port");
            }

            m_context.service_center_host[i] = new SFServerInfo(server[0].trim(), Integer.parseInt(server[1].trim()));
            m_context.service_center[i] = new InetSocketAddress(server[0].trim(), Integer.parseInt(server[1].trim()));
        }

        m_context.send_recv_buf = new byte[max_pkg_size];
    }

    public void reconnectServerManager(PSFClientConnectionInfo info,
                                           String service_type) throws IOException
    {
        boolean ret = false;
        PSFProtoHeader header;

        for (int i = 0; i < m_context.reconnect_times; i++) {
            try {
                info.conn.sock = connectServer(new InetSocketAddress(info.conn.ip_addr.trim(), info.conn.port));
                ret = true;
                break;
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException err) {}
            }
        }

        if (ret == false) {
            info.reallocate = !info.forbidden_alloc_server;
            throw new IOException("failed to connect server manager");
        }

        try {
            int len = buildClientJoinPackage(m_context, service_type);

            OutputStream out = info.conn.sock.getOutputStream();
            out.write(m_context.send_recv_buf, 0, len);

            InputStream in = info.conn.sock.getInputStream();
            header = recvHeader(in);
            if (header.status != 0) {
                throw new IOException("recv header status != 0 from server manager.");
            }
        } catch (IOException e) {
            info.reallocate = !info.forbidden_alloc_server;
            info.conn.sock.close();
            info.conn.sock = null;
            throw e;
        }
    }

    public boolean connectServiceCenter(PSFClientContext context,
                                            ConnectionInfo conn)
    {
        int host_id;
        boolean ret = false;

        host_id = context.last_connect_host_id + 1;
        for (int i = 0; i < context.service_center_host_number; i++) {
            host_id = (host_id + i) % context.service_center_host_number;
            conn.sock = null;
            conn.port = context.service_center_host[host_id].port;
            conn.ip_addr = context.service_center_host[host_id].ip_addr;

            try {
                conn.sock = connectServer(new InetSocketAddress(conn.ip_addr.trim(), conn.port));
                context.last_connect_host_id = host_id;
                ret = true;
            } catch (IOException e) {}
        }

        return ret;
    }

    public byte[] recvBody(InputStream in, int bodyLen) throws IOException
    {
        int bytes;
        byte[] data = new byte[bodyLen];

        if ((bytes = in.read(data)) != data.length) {
            throw new IOException("recv package size " + bytes + " != " + data.length);
        }

        return data;
    }

    public PSFProtoHeader recvHeader(InputStream in) throws IOException
    {
        int bytes;
        byte[] recv_buff = new byte[10];

        if ((bytes = in.read(recv_buff)) != recv_buff.length) {
            throw new IOException("recv package size " + bytes + " != " + recv_buff.length);
        }

        PSFProtoHeader header = new PSFProtoHeader();
        header.magic_number = buff2int(recv_buff, 0);
        header.func_id = recv_buff[4];
        header.status = recv_buff[5];
        header.body_len = buff2int(recv_buff, 6);

        if (header.magic_number != PSF_PROTO_MAGIC_NUMBER) {
            throw new IOException("recv package magic_number " + header.magic_number + " != " + PSF_PROTO_MAGIC_NUMBER);
        }

        if (header.body_len < 0) {
            throw new IOException("recv package body_len " + header.body_len + " < 0");
        }

        return header;
    }


    public void allocateFromServiceCenter(PSFClientConnectionInfo ClientInfo,
                                              String serivce_type) throws IOException
    {
        boolean ret;
        byte[] recvBuff = new byte[0];
        ConnectionInfo serviceCenterConn = new ConnectionInfo();

        ret = connectServiceCenter(m_context, serviceCenterConn);
        if (ret == false) {
            throw new IOException("failed to connect to service center");
        }

        try {
            int len = buildAllocatePackage(m_context, serivce_type);

            OutputStream out = serviceCenterConn.sock.getOutputStream();
            out.write(m_context.send_recv_buf, 0, len);

            InputStream in = serviceCenterConn.sock.getInputStream();

            PSFProtoHeader header = recvHeader(in);
            if (header.body_len > 0) {
                recvBuff = recvBody(in, header.body_len);
            }

            if (header.status != 0) {
                throw new IOException("recv package status " + header.status + " != 0, error: " + recvBuff.toString());
            }

            if (header.body_len <= 0) {
                throw new IOException("recv package body_len " + header.body_len + " < 0");
            }

            /* server manager ip & port */
            ClientInfo.conn.port = buff2short(recvBuff, 0);
            ClientInfo.conn.ip_addr = new String(recvBuff, 3, recvBuff[2]);
        } catch (IOException e) {
            serviceCenterConn.sock.close();
            serviceCenterConn.sock = null;
            throw e;
        }

        serviceCenterConn.sock.close();
        serviceCenterConn.sock = null;
    }

    public void reconnectServerManagerEx(PSFClientConnectionInfo info,
                                            String service_type) throws IOException
    {
        try {
            reconnectServerManager(info, service_type);
        } catch (IOException e) {
            checkAllocAndConnectServerManager(info, service_type);
        }
    }

    public int checkAllocAndConnectServerManager(PSFClientConnectionInfo info,
                                                        String service_type) throws IOException
    {
        if (info.reallocate) {
            allocateFromServiceCenter(info, service_type);
            info.reallocate = false;
            info.forbidden_alloc_server = false;
        }

        reconnectServerManager(info, service_type);

        return 0;
    }

    public PSFClientConnectionInfo getServerManagerConn(PSFClientContext context,
                                                             String service_type) throws IOException
    {
        PSFClientConnectionInfo connInfo = null;

        if (context.server_manager_hash.containsKey(service_type)) {
            connInfo = (PSFClientConnectionInfo) context.server_manager_hash.get(service_type);
        }

        if (connInfo == null) {
            connInfo = new PSFClientConnectionInfo();
            connInfo.reallocate = true;
            connInfo.forbidden_alloc_server = false;
            connInfo.conn = new ConnectionInfo();
            connInfo.conn.sock = null;

            checkAllocAndConnectServerManager(connInfo, service_type);

            context.server_manager_hash.put(service_type, connInfo);
        } else if (connInfo.conn.sock == null) {
            checkAllocAndConnectServerManager(connInfo, service_type);
        }

        return connInfo;
    }

    public String executeRpcCall(PSFClientConnectionInfo clientInfo,
                                  PSFRPCRequestData request, String service_type) throws IOException
    {
        PSFProtoHeader header;
        byte[] data;
        String response = null;
        boolean retry = true;
        InputStream in;

        do {
            try {
                int len = buildRpcRequestPackage(m_context, request);

                try {
                    OutputStream out = clientInfo.conn.sock.getOutputStream();
                    out.write(m_context.send_recv_buf, 0, len);
                } catch (IOException e) {
                    if (retry) {
                        retry = false;
                        reconnectServerManagerEx(clientInfo, service_type);
                        continue;
                    }
                    throw e;
                }

                try {
                    in = clientInfo.conn.sock.getInputStream();
                    header = recvHeader(in);
                } catch (IOException e) {
                    if (retry) {
                        retry = false;
                        clientInfo.reallocate = !clientInfo.forbidden_alloc_server;
                        checkAllocAndConnectServerManager(clientInfo, service_type);
                        continue;
                    }
                    throw e;
                }

                if (header.status != 0) {
                    throw new IOException("failed to connect to service manager");
                }

                if (header.body_len > 0) {
                    data = recvBody(in, header.body_len);
                    response = new String(data);
                }
            } catch (IOException e) {
                clientInfo.conn.sock.close();
                clientInfo.conn.sock = null;
                throw e;
            }

            break;
        } while (true);

        return response;
    }

    public int buildRpcRequestPackage(PSFClientContext context,
                                         PSFRPCRequestData request) throws UnsupportedEncodingException
    {
        int req_data_len;
        int service_uri_len;
        int header_count;
        int header_len;
        int func_id;
        int body_len;
        byte[] data;
        StringBuilder str = new StringBuilder();

        if (request.headers != null && (header_count = request.headers.size()) > 0) {
            Enumeration e = request.headers.keys();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                str.append(key + "=" + request.headers.get(key));
                if (--header_count > 0) {
                    str.append("&");
                }
            }
            header_len = str.toString().length();
        } else {
            header_len = 0;
        }

        req_data_len = request.data.getBytes(PSF_DEFAULT_CHARSET).length;
        service_uri_len = request.service_uri.getBytes(PSF_DEFAULT_CHARSET).length;

        body_len = 2 + service_uri_len + req_data_len;
        if (header_len == 0) {
            func_id = PSF_PROTO_FID_RPC_REQ_NO_HEADER;
        } else {
            func_id = PSF_PROTO_FID_RPC_REQ_WITH_HEADER;
            body_len += 2 + header_len;
        }

        int offset = buildPsfProtoHeader(context.send_recv_buf,
                func_id, 0, body_len);

        if (header_len > 0) {
            data = short2buff((short) header_len);
            System.arraycopy(data, 0, context.send_recv_buf, offset, 2);
            offset += 2;
        }

        data = short2buff((short) service_uri_len);
        System.arraycopy(data, 0, context.send_recv_buf, offset, 2);
        offset += 2;

        data = request.service_uri.getBytes(PSF_DEFAULT_CHARSET);
        System.arraycopy(data, 0, context.send_recv_buf, offset, service_uri_len);
        offset += service_uri_len;

        data = request.data.getBytes(PSF_DEFAULT_CHARSET);
        System.arraycopy(data, 0, context.send_recv_buf, offset, req_data_len);
        offset += req_data_len;

        if (header_len > 0) {
            data = request.data.getBytes(PSF_DEFAULT_CHARSET);
            System.arraycopy(data, 0, context.send_recv_buf, offset, data.length);
            offset += data.length;
        }

        return offset;
    }

    public int buildPsfProtoHeader(byte[] header, int func_id, int status, int body_len)
    {
        byte[] hex_len;
        int offset;

        offset = 0;
        hex_len = int2buff(PSF_PROTO_MAGIC_NUMBER);
        System.arraycopy(hex_len, 0, header, offset, hex_len.length);
        offset += hex_len.length;
        header[offset++] = (byte)func_id;
        header[offset++] = (byte)status;
        hex_len = int2buff(body_len);
        System.arraycopy(hex_len, 0, header, offset, hex_len.length);
        offset += hex_len.length;

        return offset;
    }

    public int buildAllocatePackage(PSFClientContext context, String service_type)
            throws UnsupportedEncodingException
    {
        byte[] data;
        int body_len = 1/* service_type_len(1byte) */ + service_type.length();

        int offset = buildPsfProtoHeader(context.send_recv_buf,
                PSF_PROTO_FID_ALLOC_SERVER, 0, body_len);

        context.send_recv_buf[offset++] = (byte) service_type.length();
        data = service_type.getBytes(PSF_DEFAULT_CHARSET);
        System.arraycopy(data, 0, context.send_recv_buf, offset, data.length);
        offset += data.length;

        return offset;
    }

    public int buildClientJoinPackage(PSFClientContext context, String service_type)
            throws UnsupportedEncodingException
    {
        byte[] data;

        int body_len = 1 /* service_type_len(1byte) */ + service_type.length();

        int offset = buildPsfProtoHeader(context.send_recv_buf,
                PSF_PROTO_FID_CLIENT_JOIN, 0, body_len);

        context.send_recv_buf[offset++] = (byte) service_type.length();

        data = service_type.getBytes(PSF_DEFAULT_CHARSET);
        System.arraycopy(data, 0, context.send_recv_buf, offset, data.length);
        offset += data.length;

        return offset;
    }

    private Socket connectServer(InetSocketAddress sockAddr) throws IOException
    {
        Socket sock = new Socket();
        sock.setSoTimeout(m_context.network_timeout * 1000);
        sock.connect(sockAddr, m_context.connect_timeout * 1000);

        return sock;
    }

    /**
     * buff convert to int
     * @param bs the buffer (big-endian)
     * @param offset the start position based 0
     * @return int number
     */
    public static int buff2int(byte[] bs, int offset)
    {
        return  (((int)(bs[offset] >= 0 ? bs[offset] : 256+bs[offset])) << 24) |
                (((int)(bs[offset+1] >= 0 ? bs[offset+1] : 256+bs[offset+1])) << 16) |
                (((int)(bs[offset+2] >= 0 ? bs[offset+2] : 256+bs[offset+2])) <<  8) |
                ((int)(bs[offset+3] >= 0 ? bs[offset+3] : 256+bs[offset+3]));
    }

    /**
     * int convert to buff (big-endian)
     * @param n number
     * @return 4 bytes buff
     */
    public static byte[] int2buff(int n)
    {
        byte[] bs;

        bs = new byte[4];
        bs[0] = (byte)((n >> 24) & 0xFF);
        bs[1] = (byte)((n >> 16) & 0xFF);
        bs[2] = (byte)((n >> 8) & 0xFF);
        bs[3] = (byte)(n & 0xFF);

        return bs;
    }

    /**
     * buff convert to int
     * @param bs the buffer (big-endian)
     * @param offset the start position based 0
     * @return int number
     */
    public static int buff2short(byte[] bs, int offset)
    {
        return (((int)(bs[offset] >= 0 ? bs[offset] : 256+bs[offset])) << 8) |
               ((int)(bs[offset+1] >= 0 ? bs[offset+1] : 256+bs[offset+1]));
    }

    public static byte[] short2buff(short n)
    {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) ((n >> 8) & 0xff);
        return b;
    }
}
