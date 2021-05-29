package utility;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author kangmoo Heo
 */
public class UdpSendTester {
    public static void main(String[] args) {
        try {
            int fromPort = Integer.parseInt(args[0]);
            String toIp = args[1];
            int toPort = Integer.parseInt(args[2]);

            InetAddress ia = InetAddress.getByName(toIp);
            DatagramSocket ds = new DatagramSocket(fromPort);

            for (int i = 3; i < args.length; i++) {
                String msg = FileUtil.fileToString(new File(args[i]));
                byte[] buffer = msg.getBytes();
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ia, toPort);
                ds.send(dp);
                System.out.println("Send UDP Msg [" + msg + "]");
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}