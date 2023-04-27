package com.github.kangmoo.utils.udp;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;

/**
 * @author kangmoo Heo
 */
public class UdpClient implements Closeable {
    private final Integer fromPort;
    private final String toIp;
    private final int toPort;
    private InetAddress ia;
    private DatagramSocket ds;

    public UdpClient(int fromPort, String toIp, int toPort) {
        this.fromPort = fromPort;
        this.toIp = toIp;
        this.toPort = toPort;
    }

    public UdpClient(String toIp, int toPort) {
        this.fromPort = null;
        this.toIp = toIp;
        this.toPort = toPort;
    }

    public void connect() throws SocketException, UnknownHostException {
        this.ds = fromPort == null ? new DatagramSocket() : new DatagramSocket(this.fromPort);
        this.ia = InetAddress.getByName(this.toIp);
    }

    public void send(byte[] data) throws IOException {
        if (this.ds == null) return;
        DatagramPacket dp = new DatagramPacket(data, data.length, this.ia, this.toPort);
        this.ds.send(dp);
    }

    public void send(String msg) throws IOException {
        this.send(msg.getBytes());
    }

    public void disconnect() {
        this.ds.close();
    }

    @Override
    public void close() throws IOException {
        if (this.ds != null)
            this.disconnect();
    }
}
