package com.github.kangmoo.utils.utility;

import com.github.kangmoo.utils.rabbitmq.RmqAlarm;
import com.github.kangmoo.utils.rabbitmq.RmqCallback;
import com.github.kangmoo.utils.rabbitmq.RmqSender;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author kangmoo Heo
 */
public class RmqSendTester {
    public static void main(String[] args) {
        RmqSender rmqSender = null;
        try {
            rmqSender = new RmqSender(args[0], args[1], args[2], args[3], new MessageCallback());
            rmqSender.connectClient();
            for (int i = 4; i < args.length; i++) {
                String json = FileUtil.fileToString(new File(args[i]));
                byte[] msg = json.getBytes(Charset.defaultCharset());
                rmqSender.send(msg, msg.length);
                System.out.println("Send Rmq Msg [" + json + "]");
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rmqSender != null) rmqSender.close();
    }

    private static class MessageCallback implements RmqCallback {
        @Override
        public void onReceived(String msg, Date ts) {
            System.out.println("msg = " + msg + ", ts = " + ts);
        }

        @Override
        public void onAlarmNotify(RmqAlarm level, Throwable e) {
            System.out.println("level = " + level + ", e = " + e);
        }
    }
}