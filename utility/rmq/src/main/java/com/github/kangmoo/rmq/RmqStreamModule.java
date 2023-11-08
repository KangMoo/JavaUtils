package com.github.kangmoo.rmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * @author kangmoo Heo
 */
@Slf4j
public class RmqStreamModule {
    private static final String STREAM_OFFSET = "x-stream-offset";
    private final RmqModule rmqModule;
    private final int qos;
    private final Object streamOffset;

    public RmqStreamModule(RmqModule rmqModule, int qos, Object streamOffset) {
        this.rmqModule = rmqModule;
        this.qos = qos;
        this.streamOffset = streamOffset;
    }

    RmqStreamModule(String host, String userName, String password, int port, int bufferCount, int recoveryInterval, int requestedHeartbeat, int connectionTimeout, int qos, Object streamOffset, Runnable onConnected, Runnable onDisconnected) {
        this(new RmqModule(host, userName, password, port, bufferCount, recoveryInterval, requestedHeartbeat, connectionTimeout, onConnected, onDisconnected), qos, streamOffset);
    }

    public static RmqStreamModuleBuilder builder(String host, String userName, String password) {
        return new RmqStreamModuleBuilder(host, userName, password);
    }


    public ScheduledExecutorService getConnectRetryThread() {
        return rmqModule.getConnectRetryThread();
    }

    public int getRequestedHeartbeat() {
        return rmqModule.getRequestedHeartbeat();
    }

    public String getHost() {
        return rmqModule.getHost();
    }

    public String getUserName() {
        return rmqModule.getUserName();
    }

    public String getPassword() {
        return rmqModule.getPassword();
    }

    public ScheduledExecutorService getRmqSender() {
        return rmqModule.getRmqSender();
    }

    public Runnable getOnDisconnected() {
        return rmqModule.getOnDisconnected();
    }

    public ArrayBlockingQueue<Runnable> getQueue() {
        return rmqModule.getQueue();
    }

    public Channel getChannel() {
        return rmqModule.getChannel();
    }

    public Connection getConnection() {
        return rmqModule.getConnection();
    }

    public Runnable getOnConnected() {
        return rmqModule.getOnConnected();
    }

    public int getPort() {
        return rmqModule.getPort();
    }

    public int getBufferCount() {
        return rmqModule.getBufferCount();
    }

    public int getRecoveryInterval() {
        return rmqModule.getRecoveryInterval();
    }

    public int getConnectionTimeout() {
        return rmqModule.getConnectionTimeout();
    }

    public void connect() throws IOException, TimeoutException {
        rmqModule.connect();
        rmqModule.getChannel().basicQos(qos);
    }

    @Synchronized
    public void connectWithAsyncRetry() {
        rmqModule.connectWithAsyncRetry();
    }

    public void queueDeclare(String queueName) throws IOException {
        rmqModule.queueDeclare(queueName, Map.of("x-queue-type", "stream"));
    }

    public void queueDeclare(String queueName, long maxLengthBytes, @NonNull String maxAge) throws IOException {
        rmqModule.queueDeclare(queueName, Map.of("x-queue-type", "stream",
                "x-max-length-bytes", maxLengthBytes,
                "x-max-age", maxAge));
    }

    public void queueDeclare(String queueName, Map<String, Object> arguments) throws IOException {
        rmqModule.queueDeclare(queueName, arguments);
    }

    public void queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws IOException {
        rmqModule.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
    }

    public void sendMessage(String queueName, String message) {
        rmqModule.sendMessage(queueName, message);
    }

    public void sendMessage(String queueName, String message, int expiration) {
        rmqModule.sendMessage(queueName, message, expiration);
    }

    public void sendMessage(String queueName, byte[] message) {
        rmqModule.sendMessage(queueName, message);
    }

    public void sendMessage(String queueName, byte[] message, int expiration) {
        rmqModule.sendMessage(queueName, message, expiration);
    }

    @Synchronized
    public void registerConsumer(String queueName, DeliverCallback deliverCallback, Map<String, Object> arguments) throws IOException {
        if (arguments == null) {
            arguments = Map.of(STREAM_OFFSET, streamOffset);
        }
        rmqModule.getChannel().basicConsume(queueName, false, arguments, deliverCallback, consumerTag -> {
        });
    }

    /**
     * @param streamOffset first - 로그에서 사용 가능한 첫 번째 메시지부터 시작
     *                     last - 메시지의 마지막으로 작성된 "청크"에서 읽기 시작
     *                     next - 오프셋을 지정하지 않은 것과 동일
     */
    public void registerConsumer(String queueName, DeliverCallback deliverCallback, String streamOffset) throws IOException {
        this.registerConsumer(queueName, deliverCallback, Map.of(STREAM_OFFSET, streamOffset));
    }

    /**
     * @param streamOffset 로그에서 오프셋의 메시지부터 시작
     */
    public void registerConsumer(String queueName, DeliverCallback deliverCallback, int streamOffset) throws IOException {
        this.registerConsumer(queueName, deliverCallback, Map.of(STREAM_OFFSET, streamOffset));
    }

    /**
     * @param streamOffset 로그에 첨부할 시점을 지정하는 타임스탬프 값
     */
    public void registerConsumer(String queueName, DeliverCallback deliverCallback, Date streamOffset) throws IOException {
        this.registerConsumer(queueName, deliverCallback, Map.of(STREAM_OFFSET, streamOffset));
    }

    public void registerStringConsumer(String queueName, Consumer<String> msgCallback) throws IOException {
        this.registerConsumer(queueName, (s, delivery) -> msgCallback.accept(new String(delivery.getBody(), StandardCharsets.UTF_8)), (Map) null);
    }

    public void registerByteConsumer(String queueName, Consumer<byte[]> msgCallback) throws IOException {
        this.registerConsumer(queueName, (s, delivery) -> msgCallback.accept(delivery.getBody()), (Map) null);
    }



    public boolean isConnected() {
        return rmqModule.isConnected();
    }

    @Synchronized
    public void close() {
        rmqModule.close();
    }
}
