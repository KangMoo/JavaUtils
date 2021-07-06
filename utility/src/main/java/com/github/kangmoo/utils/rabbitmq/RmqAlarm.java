package com.github.kangmoo.utils.rabbitmq;

/**
 * RabbitMQ Alarm 관련 define
 */
public enum RmqAlarm {
    UNKNOWN(-1),
    NOR(0),
    MIN(1),
    MAJ(2),
    CRI(3);

    private final Integer value;

    private RmqAlarm(Integer i) {
        this.value = i;
    }
}
