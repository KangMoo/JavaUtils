package test;

import org.junit.Test;
import udp.UdpClient;
import udp.UdpServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author kangmoo Heo
 */
public class UdpTest {
    @Test
    public void udpTest() throws SocketException {
        UdpServer udpServer = new UdpServer(7000, (packet) -> System.out.println("packet 수신. " + new String(packet, StandardCharsets.UTF_8)));
        udpServer.start();
        UdpClient udpClient = new UdpClient(7001, "127.0.0.1", 7000);
        try {
            udpClient.connect();
            udpClient.simpleSend("Hey!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
