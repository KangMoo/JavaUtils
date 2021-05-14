package rqbbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * RabbitMQ receiver
 *
 * @author Donghyun KIM
 * @since 0.9.0
 */
public class RmqReceiver extends RmqTransport {

    private static final Logger logger = LoggerFactory.getLogger(RmqReceiver.class);

    RmqCallback callback;
    private Consumer consumer = new DefaultConsumer(getChannel()) {
        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
            String msg = new String(body, StandardCharsets.UTF_8);

            if (callback != null) {
                Date ts = null;
                Map<String, Object> headers = properties.getHeaders();
                if (headers != null) {
                    Long ms = (Long) headers.get("timestamp_in_ms");
                    if (ms != null) {
                        ts = new Date(ms);
                    }
                }
                callback.onReceived(msg, ts);
            }
        }
    };

    /**
     * constructor by RMQ information
     *
     * @param host      RMQ host
     * @param userName  RMQ user name
     * @param password  RMQ password
     * @param queueName RMQ queue name
     * @param callback  RMQ message receive callback
     */
    public RmqReceiver(String host, String userName, String password, String queueName, RmqCallback callback) {
        super(host, userName, password, queueName, callback);
        this.callback = callback;
    }

    /**
     * RMQ start
     *
     * @return success/fail
     */
    public boolean start() {

        if (!isConnected()) {
            logger.error("() () () channel is not opened. [RMQ name: {}]", getQueueName());
            return false;
        }

        try {
            getChannel().basicQos(10);
            getChannel().basicConsume(getQueueName(), true, consumer);
        } catch (Exception e) {
            logger.warn("() () () Exception to basicConsume. [RMQ name: {}]", getQueueName());
            return false;
        }
        return true;
    }
}