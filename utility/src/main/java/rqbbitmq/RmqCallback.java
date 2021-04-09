/*
 * Copyright (C) 2020. Uangel Corp. All rights reserved.
 *
 */
package rqbbitmq;

import java.util.Date;

/**
 * RabbitMq callback interface
 *
 * @author Donghyun KIM
 * @since 0.9.0
 */
public interface RmqCallback {

    /**
     * RMQ message 수신 시 호출한다.
     *
     * @param msg RMQ message
     * @param ts  received time
     */
    void onReceived (String msg, Date ts);

    /**
     * RMQ 장애 또는 복구 시 장애 알람을 수신한다.
     *
     * @param level     alarm level
     * @param exception alarm exception
     */
    void onAlarmNotify (RmqAlarm level, Throwable exception);
}
