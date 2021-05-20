package udp;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author kangmoo Heo
 */
public class UdpClient {
    private int fromPort;
    private String toIp;
    private int toPort;
    private InetAddress ia;
    private DatagramSocket ds;

    public UdpClient(int fromPort, String toIp, int toPort) {
        this.fromPort = fromPort;
        this.toIp = toIp;
        this.toPort = toPort;
    }

    public void connect() throws SocketException, UnknownHostException {
        this.ds = new DatagramSocket(this.fromPort);
        this.ia = InetAddress.getByName(this.toIp);
    }

    public void simpleSend(byte[] data) throws IOException {
        if (this.ds == null) return;
        DatagramPacket dp = new DatagramPacket(data, data.length, this.ia, this.toPort);
        this.ds.send(dp);
    }

    public void simpleSend(String msg) throws IOException {
        this.simpleSend(msg.getBytes());
    }

    public void disconnect() {
        this.ds.close();
    }
}
