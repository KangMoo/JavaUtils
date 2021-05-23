package test;

import org.junit.Test;
import udp.UdpClient;
import udp.UdpConn;
import udp.UdpServer;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author kangmoo Heo
 */
public class UdpTest {
    String sendMsg = "Hello";
    String recvMsg = null;
    @Test
    public void udpTest() throws SocketException {
        UdpServer udpServer = new UdpServer(7000, (packet) -> {
            String inputMsg = new String(packet, StandardCharsets.UTF_8);
            System.out.println("packet Recv. " + inputMsg);
            this.recvMsg = inputMsg;
        });
        udpServer.start();
        UdpClient udpClient = new UdpClient(7001, "127.0.0.1", 7000);

        try {
            udpClient.connect();
            udpClient.simpleSend(sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(100);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void udpTest2() throws IOException {
        UdpConn udpConn1 = new UdpConn(7777, (packet) -> {
            String inputMsg = new String(packet, StandardCharsets.UTF_8);
            System.out.println("[7777] packet Recv. " + inputMsg);
        });
        UdpConn udpConn2 = new UdpConn(8888, (packet) -> {
            String inputMsg = new String(packet, StandardCharsets.UTF_8);
            System.out.println("[8888] packet Recv. " + inputMsg);
        });

        udpConn1.sendData("Hello", "127.0.0.1", 8888);
        udpConn2.sendData("World", "127.0.0.1", 7777);

        try {
            Thread.sleep(100);
            assert !sendMsg.equals(recvMsg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
