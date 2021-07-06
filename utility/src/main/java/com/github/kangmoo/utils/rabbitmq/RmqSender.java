package com.github.kangmoo.utils.rabbitmq;

import com.rabbitmq.client.AMQP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RabbitMQ sender
 *
 * @author Donghyun KIM
 * @since 0.9.0
 */
public class RmqSender extends RmqTransport {

    private static final Logger logger = LoggerFactory.getLogger(RmqSender.class);
    private AMQP.BasicProperties properties;

    /**
     * constructor by RMQ information
     *
     * @param host      RMQ host
     * @param userName  RMQ user name
     * @param password  RMQ password
     * @param queueName RMQ message send queue name
     */
    public RmqSender(String host, String userName, String password, String queueName, RmqCallback callback) {
        super(host, userName, password, queueName, callback);
        properties = new AMQP.BasicProperties.Builder().expiration("5000").build();
    }

    /**
     * RMQ message send
     *
     * @param msg  RMQ message
     * @param size RMQ message size
     * @return success/fail
     */
    public boolean send(byte[] msg, int size) {

        if (!isConnected() || !isSendAvailable()) {
            logger.error("() () () channel is not opened or not available. [RMQ name: {}]", getQueueName());
            return false;
        }

        if ((size <= 0) || (msg == null) || (msg.length < size)) {
            logger.warn("() () () invalid message. [size={}, msg.length={} RMQ name: {}]", size, (msg != null) ? msg.length : 0, getQueueName());
            return false;
        }

        try {
            getChannel().basicPublish("", getQueueName(), properties, msg);
        } catch (Exception e) {
            logger.warn("() () () Exception to basicPublish.", e);
            return false;
        }
        return true;
    }
}
