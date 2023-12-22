package com.github.kangmoo.rmq;

import static com.rabbitmq.client.ConnectionFactory.USE_DEFAULT_PORT;

/**
 * @author kangmoo Heo
 */
public final class RmqModuleBuilder {
    String host;
    String userName;
    String password;
    int port = USE_DEFAULT_PORT;
    int bufferCount = 1024;
    int recoveryInterval = 1000; // RabbitMQ 서버와의 연결을 재시도하는 간격(단위:ms)
    int requestedHeartbeat = 5; // RabbitMQ 서버에게 전송하는 heartbeat 요청의 간격(단위:sec)
    int connectionTimeout = 2000; // RabbitMQ 서버와 연결을 시도하는 최대 시간(단위:ms)
    int qos = 100;
    Runnable onConnected = () -> {};
    Runnable onDisconnected = () -> {};

    RmqModuleBuilder(String host, String userName, String password) {
        this.host = host;
        this.userName = userName;
        this.password = password;
    }

    public RmqModuleBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public RmqModuleBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public RmqModuleBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public RmqModuleBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public RmqModuleBuilder setBufferCount(int bufferCount) {
        this.bufferCount = bufferCount;
        return this;
    }

    public RmqModuleBuilder setRecoveryInterval(int recoveryInterval) {
        this.recoveryInterval = recoveryInterval;
        return this;
    }

    public RmqModuleBuilder setRequestedHeartbeat(int requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
        return this;
    }

    public RmqModuleBuilder setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public RmqModuleBuilder setQos(int qos) {
        this.qos = qos;
        return this;
    }

    public RmqModuleBuilder setOnConnected(Runnable onConnected) {
        this.onConnected = onConnected;
        return this;
    }

    public RmqModuleBuilder setOnDisconnected(Runnable onDisconnected) {
        this.onDisconnected = onDisconnected;
        return this;
    }

    public RmqModule build() {
        return new RmqModule(host, userName, password, port, bufferCount, recoveryInterval, requestedHeartbeat, connectionTimeout, onConnected, onDisconnected, qos);
    }
}
