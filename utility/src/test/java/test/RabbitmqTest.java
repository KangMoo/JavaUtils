package test;

import com.rabbitmq.client.AMQP;
import org.junit.Test;
import rqbbitmq.*;

import java.util.Date;

/**
 *
 * @author kangmoo Heo
 */
public class RabbitmqTest {

    private String host = "127.0.0.1";       // Put Rmq Info
    private String userName = "userName";    // Put Rmq Info
    private String password = "password";    // Put Rmq Info
    private String queueName = "queueName";  // Put Rmq Info

    private RmqReceiver rmqReceiver;
    private RmqSender rmqSender;

    @Test
    public void rabbitmqTest() {
        startRmqServer();
        startRmqClient();

        // Message Send
        for (int i = 0; i < 10; i++) {
            String msg = "Test Msg_" + i;
            byte[] msgByte = msg.getBytes();
            rmqSender.send(msgByte, msgByte.length);
        }

        // Waiting to receive a message(1 sec)
        try {
            Thread.sleep(1000);
        } catch (Exception e){
            // Do nothing
        }
    }

    private void startRmqServer() {
        rmqReceiver = new RmqReceiver(host, userName, password, queueName, new RmqCallback() {
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
    }

    private void startRmqClient() {
        rmqSender = new RmqSender(host, userName, password, queueName, new RmqCallback() {
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
    }

}
