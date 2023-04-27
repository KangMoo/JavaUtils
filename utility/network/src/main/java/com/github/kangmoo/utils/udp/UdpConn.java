package com.github.kangmoo.utils.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.function.Consumer;

/**
 * @author kangmoo Heo
 */
public class UdpConn {
    private DatagramSocket ds;
    private Consumer<byte[]> onPacketRecv;
    private Thread handler;

    public UdpConn(int port) throws SocketException {
        this.init(port, null);
    }

    public UdpConn(int port, Consumer<byte[]> packetConsumer) throws SocketException {
        this.init(port, packetConsumer);
    }

    public void init(int port, Consumer<byte[]> packetConsumer) throws SocketException {
        this.ds = new DatagramSocket(port);
        this.onPacketRecv = packetConsumer;
        startServer();
    }

    public void startServer() {
        this.handler = new Thread(this::packetHandle);
        this.handler.start();
    }

    public void packetHandle() {
        DatagramPacket dp = new DatagramPacket(new byte[66536], 66536);
        while (ds != null) {
            try {
                this.ds.receive(dp);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (onPacketRecv == null) continue;
            byte[] data = new byte[dp.getLength()];
            System.arraycopy(dp.getData(), 0, data, 0, dp.getLength());
            onPacketRecv.accept(data);
        }
    }

    public void setRecvCallBack(Consumer<byte[]> consumer) {
        this.onPacketRecv = consumer;
        if (handler == null || handler.isInterrupted()) {
            startServer();
        }
    }

    public void sendData(byte[] data, InetSocketAddress dest) throws IOException {
        if (this.ds == null) return;
        DatagramPacket dp = new DatagramPacket(data, data.length, dest);
        this.ds.send(dp);
    }

    public void sendData(byte[] data, String ip, int port) throws IOException {
        sendData(data, new InetSocketAddress(ip, port));
    }

    public void sendData(String msg, String ip, int port) throws IOException {
        this.sendData(msg.getBytes(), ip, port);
    }

    public void sendData(String msg, InetSocketAddress dest) throws IOException {
        this.sendData(msg.getBytes(), dest);
    }

    public void disconnect() {
        if (this.ds != null && this.ds.isConnected()) ds.close();
        this.handler.interrupt();
    }
}
