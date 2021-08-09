package com.github.kangmoo.utils.udp;

import java.net.*;
import java.util.function.Consumer;

/**
 *
 * @author kangmoo Heo
 */
public class UdpServer extends Thread{
    private boolean running = false;
    private DatagramSocket ds;
    private Consumer<byte[]> packetConsumer;
    public UdpServer(int port, Consumer<byte[]> packetCallback) throws SocketException {
        this.ds = new DatagramSocket(port);
        this.packetConsumer = packetCallback;
    }

    public void run() {
        running = true;
        DatagramPacket dp = new DatagramPacket(new byte[66536], 66536);
        try {
            this.ds.setSoTimeout(1000);
            while(running){
                if(packetConsumer == null) continue;
                try {
                    this.ds.receive(dp);
                } catch (Exception e) {
                    e.printStackTrace();
                    // do nothing
                }
                byte[] data = new byte[dp.getLength()];
                System.arraycopy(dp.getData(), 0, data, 0, dp.getLength());
                packetConsumer.accept(data);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public boolean isRunning() {
        return running;
    }

    public void stopServer() {
        this.ds.disconnect();
        this.running = false;
    }

    public Consumer<byte[]> getPacketConsumer() {
        return packetConsumer;
    }

    public UdpServer setPacketConsumer(Consumer<byte[]> packetConsumer) {
        this.packetConsumer = packetConsumer;
        return this;
    }
}
