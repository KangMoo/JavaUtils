package udp;

import java.io.IOException;
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
        while(running){
            if(packetConsumer == null) continue;
            try {
                this.ds.receive(dp);
            } catch (Exception e) {
                e.printStackTrace();
                // do nothing
            }
            packetConsumer.accept(dp.getData());
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void stopServer() {
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
