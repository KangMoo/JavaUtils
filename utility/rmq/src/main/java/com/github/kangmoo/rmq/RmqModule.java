package com.github.kangmoo.rmq;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.DefaultExceptionHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;


/**
 * RabbitMQ와의 연결을 관리하며, 다양한 RabbitMQ 관련 작업을 제공하는 클래스.
 * 이 클래스는 다음과 같은 기능을 수행한다:
 * <ul>
 *   <li> RabbitMQ 서버와의 연결 설정 및 종료 </li>
 *   <li> 채널 생성 </li>
 *   <li> 메시지 큐 선언 </li>
 *   <li> 지정된 큐에 메시지 전송 </li>
 *   <li> 지정된 큐에 소비자 등록 </li>
 * </ul>
 */
@Slf4j
@Getter
public class RmqModule {
    private ScheduledExecutorService connectRetryThread;

    private final String host;
    private final String userName;
    private final String password;
    private final int port;
    private final int bufferCount;
    private final int recoveryInterval; // RabbitMQ 서버와의 연결을 재시도하는 간격(단위:ms)
    private final int requestedHeartbeat; // RabbitMQ 서버에게 전송하는 heartbeat 요청의 간격(단위:sec)
    private final int connectionTimeout; // RabbitMQ 서버와 연결을 시도하는 최대 시간(단위:ms)
    private final int qos;
    @Setter
    private Runnable onConnected;
    @Setter
    private Runnable onDisconnected;

    protected final ArrayBlockingQueue<Runnable> queue;
    private ScheduledExecutorService rmqSender;

    // RabbitMQ 서버와의 연결과 채널을 관리하기 위한 변수
    protected Connection connection;
    protected Channel channel;

    RmqModule(String host, String userName, String password, int port, int bufferCount, int recoveryInterval, int requestedHeartbeat, int connectionTimeout, Runnable onConnected, Runnable onDisconnected, int qos) {
        this.host = host;
        this.userName = userName;
        this.password = password;
        this.port = port;
        this.bufferCount = bufferCount;
        this.queue = new ArrayBlockingQueue<>(this.bufferCount);
        this.recoveryInterval = recoveryInterval;
        this.requestedHeartbeat = requestedHeartbeat;
        this.connectionTimeout = connectionTimeout;
        this.onConnected = onConnected;
        this.onDisconnected = onDisconnected;
        this.qos = qos;
    }

    public static RmqModuleBuilder builder(String host, String userName, String password) {
        return new RmqModuleBuilder(host, userName, password);
    }

    /**
     * RabbitMQ 서버에 연결을 수립하며 통신을 위한 채널을 생성하고, 연결에 대한 예외 처리기를 설정하고, 만약 연결이 복구 가능한 경우, 복구 리스너도 설정한다.
     * 연결과 채널이 성공적으로 수립되면 제공된 onConnected 콜백을, 연결에 실패하면 onDisconnected 콜백을 호출한다.
     *
     * @throws IOException      연결과 채널을 생성하는 동안 I/O 에러가 발생한 경우
     * @throws TimeoutException 연결과 채널을 생성하는 동안 타임아웃이 발생한 경우
     */
    public void connect() throws IOException, TimeoutException {
        if (isConnected()) {
            log.warn("RMQ Already Connected");
            return;
        }

        try {
            ConnectionFactory factory = new ConnectionFactory();

            // RabbitMQ 서버 정보 설정
            factory.setHost(host);
            factory.setUsername(userName);
            factory.setPassword(password);
            factory.setPort(port);

            // 자동 복구를 활성화하고, 네트워크 복구 간격, heartbeat 요청 간격, 연결 타임아웃 시간을 설정
            factory.setAutomaticRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(recoveryInterval);
            factory.setRequestedHeartbeat(requestedHeartbeat);
            factory.setConnectionTimeout(connectionTimeout);

            factory.setExceptionHandler(new DefaultExceptionHandler() {
                @Override
                public void handleUnexpectedConnectionDriverException(Connection con, Throwable exception) {
                    super.handleUnexpectedConnectionDriverException(con, exception);
                    onDisconnected();
                }
            });

            // 연결과 채널 생성 시도
            this.connection = factory.newConnection();
            ((Recoverable) connection).addRecoveryListener(new RecoveryListener() {
                public void handleRecovery(Recoverable r) {
                    onConnected();
                }

                public void handleRecoveryStarted(Recoverable r) {
                    onDisconnected();
                }
            });

            this.channel = connection.createChannel();
            try {
                if (this.rmqSender != null && !this.rmqSender.isShutdown()) {
                    this.rmqSender.shutdown();
                }
            } catch (Exception e) {
                log.warn("Error while shutdown Scheduler.", e);
            }
            this.rmqSender = Executors.newSingleThreadScheduledExecutor(new BasicThreadFactory.Builder().namingPattern("RMQ_SENDER_%d").daemon(true).build());
            this.rmqSender.scheduleWithFixedDelay(() -> {
                while (true) {
                    try {
                        Runnable runnable = queue.poll();
                        if (runnable == null) {
                            return;
                        }
                        runnable.run();
                    } catch (Exception e) {
                        log.warn("Error Occurs", e);
                    }
                }
            }, 0, 10, TimeUnit.MILLISECONDS);
            channel.basicQos(qos);
            onConnected();
        } catch (Exception e) {
            onDisconnected();
            throw e;
        }
    }

    /**
     * 비동기적으로 RabbitMQ 서버에 연결을 시도하고, 연결이 실패할 경우 1초 후에 재시도한다.
     * 연결이 성공하면 onConnected 콜백을 호출하고, 연결 실패 시 onDisconnected 콜백을 호출한다.
     * 본 메서드는 스레드 안전하게 동작한다.
     */
    @Synchronized
    public void connectWithAsyncRetry() {
        try {
            if (this.connectRetryThread == null || this.connectRetryThread.isShutdown()) {
                this.connectRetryThread = Executors.newSingleThreadScheduledExecutor(new BasicThreadFactory.Builder().namingPattern("RMQ_CONNECT_THREAD_%d").daemon(true).build());
            }
            connect();
            this.connectRetryThread.shutdown();
        } catch (Exception e) {
            log.warn("Err Occurs while RMQ Connection", e);
            close();
            this.connectRetryThread.schedule(this::connectWithAsyncRetry, 1000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 지정된 이름의 메시지 큐를 생성한다.
     *
     * @param queueName 생성할 큐의 이름
     * @throws IOException 큐 생성에 실패한 경우
     */
    public void queueDeclare(String queueName) throws IOException {
        this.queueDeclare(queueName, null);
    }

    public void queueDeclare(String queueName, Map<String, Object> arguments) throws IOException {
        channel.queueDeclare(queueName, true, false, false, arguments);
    }

    public void queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws IOException {
        channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
    }

    public AMQP.Queue.DeleteOk queueDelete(String queue) throws IOException {
        return this.channel.queueDelete(queue);
    }

    public AMQP.Queue.DeleteOk queueDelete(String queue, boolean ifUnused, boolean ifEmpty) throws IOException {
        return this.channel.queueDelete(queue, ifUnused, ifEmpty);
    }

    public void queueDeleteNoWait(String queue, boolean ifUnused, boolean ifEmpty) throws IOException {
        this.channel.queueDeleteNoWait(queue, ifUnused, ifEmpty);
    }

    /**
     * 지정된 이름의 큐에 메시지를 전송한다.
     *
     * @param queueName 전송할 큐의 이름
     * @param message   전송할 메시지
     */
    public void sendMessage(String queueName, String message) {
        sendMessage(() -> {
            try {
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            } catch (Exception e) {
                log.warn("Err Occurs", e);
            }
        });
    }

    /**
     * 지정된 이름의 큐에 만료 시간과 함께 메시지를 전송한다.
     *
     * @param queueName  전송할 큐의 이름
     * @param message    전송할 메시지
     * @param expiration 메시지의 만료 시간
     */
    public void sendMessage(String queueName, String message, int expiration) {
        sendMessage(() -> {
            try {
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().expiration(Integer.toString(expiration)).build();
                channel.basicPublish("", queueName, properties, message.getBytes());
            } catch (Exception e) {
                log.warn("Err Occurs", e);
            }
        });
    }


    /**
     * 지정된 이름의 큐에 바이트 배열 형태의 메시지를 전송한다.
     *
     * @param queueName 전송할 큐의 이름
     * @param message   전송할 메시지의 바이트 배열
     */
    public void sendMessage(String queueName, byte[] message) {
        sendMessage(() -> {
            try {
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message);
            } catch (Exception e) {
                log.warn("Err Occurs", e);
            }
        });
    }

    /**
     * 지정된 이름의 큐에 만료 시간과 함께 바이트 배열 형태의 메시지를 전송한다.
     *
     * @param queueName  전송할 큐의 이름
     * @param message    전송할 메시지의 바이트 배열
     * @param expiration 메시지의 만료 시간
     */
    public void sendMessage(String queueName, byte[] message, int expiration) {
        sendMessage(() -> {
            try {
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().expiration(Integer.toString(expiration)).build();
                channel.basicPublish("", queueName, properties, message);
            } catch (Exception e) {
                log.warn("Err Occurs", e);
            }
        });
    }

    private void sendMessage(Runnable runnable) {
        if (!this.queue.offer(runnable)) {
            log.warn("RMQ SND Queue full. Drop message.");
        }
    }

    /**
     * 지정된 큐에 소비자를 등록한다.
     *
     * @param queueName       소비자를 등록할 큐의 이름
     * @param deliverCallback 메시지가 수신될 때 호출되는 콜백
     * @throws IOException 소비자 등록에 실패한 경우
     */
    @Synchronized
    public void registerConsumer(String queueName, DeliverCallback deliverCallback, Map<String, Object> arguments) throws IOException {
        channel.basicConsume(queueName, true, arguments, deliverCallback, consumerTag -> {
        });
    }

    /**
     * 지정된 큐에 문자열 형태의 메시지를 처리할 소비자를 등록한다.
     * 주로 텍스트 메시지를 받고 처리하는 경우에 사용한다.
     *
     * @param queueName   소비자를 등록할 큐의 이름
     * @param msgCallback 메시지가 수신될 때 호출되는 콜백, 메시지는 문자열로 전달됨
     * @throws IOException 소비자 등록에 실패한 경우
     */
    public void registerStringConsumer(String queueName, Consumer<String> msgCallback) throws IOException {
        registerConsumer(queueName, (s, delivery) -> msgCallback.accept(new String(delivery.getBody(), StandardCharsets.UTF_8)), null);
    }

    /**
     * 지정된 큐에 바이트 배열 형태의 메시지를 처리할 소비자를 등록한다.
     * 주로 바이너리 데이터를 받고 처리하는 경우에 사용한다.
     *
     * @param queueName   소비자를 등록할 큐의 이름
     * @param msgCallback 메시지가 수신될 때 호출되는 콜백, 메시지는 바이트 배열로 전달됨
     * @throws IOException 소비자 등록에 실패한 경우
     */
    public void registerByteConsumer(String queueName, Consumer<byte[]> msgCallback) throws IOException {
        registerConsumer(queueName, (s, delivery) -> msgCallback.accept(delivery.getBody()), null);
    }

    public boolean isConnected() {
        return this.channel != null && this.channel.isOpen() &&
                this.connection != null && this.connection.isOpen();
    }


    /**
     * RabbitMQ 서버와의 연결 및 채널을 종료한다.
     * 연결이나 채널 종료 과정에서 오류가 발생하면 로그에 출력한다.
     */
    @Synchronized
    public void close() {
        try {
            if (this.channel != null && this.channel.isOpen()) {
                this.channel.close();
            }
        } catch (Exception e) {
            log.warn("Error while closing the channel", e);
        }

        try {
            if (this.connection != null && this.connection.isOpen()) {
                this.connection.close();
            }
        } catch (Exception e) {
            log.warn("Error while closing the connection", e);
        }

        try {
            if (this.rmqSender != null && !this.rmqSender.isShutdown()) {
                this.rmqSender.shutdown();
            }
        } catch (Exception e) {
            log.warn("Error while shutdown Scheduler.", e);
        }
        log.info("RMQ Module Closed");
    }

    private void onConnected() {
        try {
            if (onConnected != null) {
                onConnected.run();
            }
        } catch (Exception e) {
            log.warn("Err Occurs", e);
        }
    }

    private void onDisconnected() {
        try {
            if (onDisconnected != null) {
                onDisconnected.run();
            }
        } catch (Exception e) {
            log.warn("Err Occurs", e);
        }
    }

    //////////////////////////////////
    // !! RMQ Stream 전용 메서드 !! //
    //////////////////////////////////

    /**
     * 지정된 이름의 메시지 스트림 큐를 생성한다.
     *
     * @param queueName      생성할 큐의 이름
     * @param maxLengthBytes 큐의 최대 총 크기(바이트)
     * @param maxAge         메시지 수명. 가능한 단위: Y, M, D, h, m, s. (e.g. 7D = 일주일)
     * @throws IOException 큐 생성에 실패한 경우
     */
    public void queueDeclareAsStream(String queueName, String maxAge, Long maxLengthBytes, Long streamMaxSegmentSizeBytes) throws IOException {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-queue-type", "stream");
        if (maxAge != null) {
            arguments.put("x-max-age", maxAge);
        }
        if (maxLengthBytes != null) {
            arguments.put("x-max-length-bytes", maxLengthBytes);
        }
        if (streamMaxSegmentSizeBytes != null) {
            arguments.put("x-stream-max-segment-size-bytes", streamMaxSegmentSizeBytes);
        }
        this.queueDeclare(queueName, arguments);
    }

    public void queueDeclareAsStream(String queueName) throws IOException {
        queueDeclareAsStream(queueName, null, null, null);
    }

    /**
     * @param streamOffset "first" - 먼저 스트림에서 사용 가능한 첫 번째 메시지부터 소비를 시작
     *                     "last" - 마지막으로 작성된 메시지 덩어리에서 소비를 시작
     *                     "next" - 스트림 끝부터 소비를 시작
     *                     Integer - 특정 오프셋에서 시작
     *                     Date - 주어진 시간부터 시작
     *                     null - "next"와 동일
     */
    @Synchronized
    public void registerConsumerAsStream(String queueName, DeliverCallback deliverCallback, @NonNull Object streamOffset) throws IOException {
        channel.basicConsume(queueName, false, Map.of("x-stream-offset", streamOffset), deliverCallback, consumerTag -> {
        });
    }

    public void registerStringConsumerAsStream(String queueName, Consumer<String> msgCallback) throws IOException {
        registerConsumerAsStream(queueName, (s, delivery) -> msgCallback.accept(new String(delivery.getBody(), StandardCharsets.UTF_8)), "next");
    }

    public void registerByteConsumerAsStream(String queueName, Consumer<byte[]> msgCallback) throws IOException {
        registerConsumerAsStream(queueName, (s, delivery) -> msgCallback.accept(delivery.getBody()), "next");
    }
}