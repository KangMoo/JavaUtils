package com.github.kangmoo.utils.rabbitmq;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.DefaultExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RabbitMQ transport
 *
 * @author Donghyun KIM
 * @since 0.9.0
 */
public class RmqTransport {

    private static final Logger logger = LoggerFactory.getLogger(RmqTransport.class);
    private final String host;
    private final String userName;
    private final String password;
    private final String queueName;
    private RmqCallback callback;
    private RmqAlarm level;
    private boolean isBlocked;

    private Connection connection;
    private Channel channel;

    /**
     * constructor by RMQ host information
     *
     * @param host      RMQ host
     * @param userName  RMQ user name
     * @param password  RMQ password
     * @param queueName RMQ queue name
     */
    public RmqTransport(String host, String userName, String password, String queueName, RmqCallback callback) {
        this.isBlocked = false;
        this.level = RmqAlarm.UNKNOWN;

        this.host = host;
        this.userName = userName;
        this.password = password;
        this.queueName = queueName;
        this.callback = callback;
    }

    /**
     * RMQ Client (receive message)
     *
     * @return connection 성공 유무
     */
    public boolean connectServer() {

        String connectionName = String.format("Consumer_%s", queueName);

        if (!makeConnection(connectionName)) {
            return false;
        }

        if (!makeChannel(true)) {
            closeConnection();
            return false;
        }
        onAlarmNotify(RmqAlarm.NOR, new Throwable("connectServer is success."));

        return true;
    }

    /**
     * RMQ Client (send message)
     *
     * @return success/fail
     */
    public boolean connectClient() {

        String connectionName = String.format("Producer_%s", queueName);
        if (!makeConnection(connectionName)) {
            return false;
        }

        if (!makeChannel(false)) {
            closeConnection();
            return false;
        }
        onAlarmNotify(RmqAlarm.NOR, new Throwable("connectClient is success."));

        return true;
    }

    /**
     * get channel
     *
     * @return channel
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * get queue name
     *
     * @return queue name
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * channel open 유무
     *
     * @return true/false
     */
    public boolean isConnected() {
        return channel != null && channel.isOpen();
    }

    /**
     * connection open 유무
     *
     * @return true/false
     */
    public boolean isConnectionOpened() {
        return connection != null && connection.isOpen();
    }

    /**
     * message 전송 가능 유무.
     *
     * @return true/false
     */
    public boolean isSendAvailable() {
        return !isBlocked;
    }

    /**
     * close RabbitMQ channel and connection
     */
    public void close() {
        closeChannel();
        closeConnection();
    }

    private boolean makeConnection(String connectionName) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(1000);
        factory.setRequestedHeartbeat(5);
        factory.setConnectionTimeout(3000);
        factory.setSocketConfigurator(socket ->{
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(3000);
        });
        factory.setExceptionHandler(new DefaultExceptionHandler() {
            @Override
            public void handleUnexpectedConnectionDriverException(Connection con, Throwable exception) {
                onAlarmNotify(RmqAlarm.CRI, exception);
            }

            @Override
            public void handleConnectionRecoveryException(Connection conn, Throwable exception) {
                logger.error("() () () connection recovery exception is occured. [RMQ name: {}]", queueName);
            }

            @Override
            public void handleChannelRecoveryException(Channel ch, Throwable exception) {
                logger.error("() () () channel recovery exception is occured. [RMQ name: {}]", queueName);
            }
        });

        try {
            connection = factory.newConnection(connectionName);
        } catch (Exception exception) {
            onAlarmNotify(RmqAlarm.CRI, exception);
            isBlocked = true;
            return false;
        }

        connection.addBlockedListener(new BlockedListener() {
            @Override
            public void handleBlocked(String s) {
                onAlarmNotify(RmqAlarm.CRI, new Throwable(s));
            }

            @Override
            public void handleUnblocked() {
                onAlarmNotify(RmqAlarm.NOR, new Throwable("Unblocked."));
            }
        });

        return true;
    }

    private void closeConnection() {
        try {
            connection.close();
        } catch (Exception exception) {
            logger.error("() () () exception to close connection is occured. [RMQ name: {}] {}", queueName, exception.getMessage());
        }
    }

    private boolean makeChannel(boolean declareQueue) {

        try {
            channel = connection.createChannel();
            ((Recoverable) channel).addRecoveryListener(new RecoveryListener() {
                @Override
                public void handleRecovery(Recoverable recoverable) {
                    if (recoverable instanceof Channel) {
                        // 자동 연결 복구 알림
                        onAlarmNotify(RmqAlarm.NOR, new Throwable("Recovery success."));
                    }
                }

                @Override
                public void handleRecoveryStarted(Recoverable recoverable) {
                    logger.error("() () () recovery started. [RMQ name: {}]", queueName);
                }
            });

            if (declareQueue) {
                channel.queueDeclare(queueName, false, false, false, null);
            }
        } catch (Exception exception) {
            onAlarmNotify(RmqAlarm.CRI, exception);
            return false;
        }
        return true;
    }

    private void closeChannel() {
        try {
            channel.close();
        } catch (Exception exception) {
            logger.error("() () () exception to close channel is occured. [RMQ name: {}] {}", queueName, exception.getMessage());
        }
    }

    private void onAlarmNotify(RmqAlarm level, Throwable exception) {
        if (callback != null && this.level != level) {
            callback.onAlarmNotify(level, exception);
            this.level = level;
        }
        if (this.level == RmqAlarm.NOR) {
            isBlocked = false;
        } else {
            isBlocked = true;
        }
    }
}
