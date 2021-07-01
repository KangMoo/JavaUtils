package test;

import org.junit.Test;
import rabbitmq.*;

import java.util.Date;
import java.util.stream.IntStream;

/**
 *
 * @author kangmoo Heo
 */
public class RabbitmqTest {

    // Put Rabbitmq Info
    private String host = "host";
    private String userName = "userName";
    private String password = "password";
    private String queueName = "queueName";

    @Test
    public void rabbitmqTest() {
        RmqReceiver rmqReceiver = startRmqServer();
        RmqSender rmqSender = startRmqClient();

        // Message Send
        IntStream.range(0, 10).mapToObj(i -> ("MSG_" + i).getBytes()).forEach(msg -> rmqSender.send(msg, msg.length));

        // Waiting to receive message(1 sec)
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            // Do nothing
        }

        rmqReceiver.close();
        rmqSender.close();
    }

    private RmqReceiver startRmqServer() {
        RmqReceiver rmqReceiver = new RmqReceiver(host, userName, password, queueName, new RmqCallback() {
            @Override
            public void onReceived(String msg, Date ts) {
                System.out.println("Recv msg = " + msg);
            }

            @Override
            public void onAlarmNotify(RmqAlarm level, Throwable exception) {
                System.out.println("level = " + level + ", exception = " + exception);
            }
        });
        rmqReceiver.connectServer();
        rmqReceiver.start();
        return rmqReceiver;
    }

    private RmqSender startRmqClient() {
        RmqSender rmqSender = new RmqSender(host, userName, password, queueName, new RmqCallback() {
            @Override
            public void onReceived(String msg, Date ts) {
                // Do nothing
            }

            @Override
            public void onAlarmNotify(RmqAlarm level, Throwable exception) {
                System.out.println("level = " + level + ", exception = " + exception);
            }
        });
        rmqSender.connectClient();
        return rmqSender;
    }

}
