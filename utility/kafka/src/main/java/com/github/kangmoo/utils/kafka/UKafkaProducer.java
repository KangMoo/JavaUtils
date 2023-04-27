/**
 * @author kangmoo Heo
 */
package com.github.kangmoo.utils.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.Serializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class UKafkaProducer<K, V> extends KafkaProducer<K, V> {
    public UKafkaProducer(Map<String, Object> configs) {
        super(configs);
    }

    public UKafkaProducer(Map<String, Object> configs, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        super(configs, keySerializer, valueSerializer);
    }

    public UKafkaProducer(Properties properties) {
        super(properties);
    }

    public UKafkaProducer(Properties properties, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        super(properties, keySerializer, valueSerializer);
    }

    public Future<RecordMetadata> send(String topic, Integer partition, Long timestamp, K key, V value) {
        return this.send(new ProducerRecord<>(topic, partition, timestamp, key, value, null));
    }

    public Future<RecordMetadata> send(String topic, Integer partition, K key, V value, Iterable<Header> headers) {
        return this.send(new ProducerRecord<>(topic, partition, null, key, value, headers));
    }

    public Future<RecordMetadata> send(String topic, Integer partition, K key, V value) {
        return this.send(new ProducerRecord<>(topic, partition, null, key, value, null));
    }

    public Future<RecordMetadata> send(String topic, K key, V value) {
        return this.send(new ProducerRecord<>(topic, null, null, key, value, null));
    }

    public Future<RecordMetadata> send(String topic, V value) {
        return this.send(new ProducerRecord<>(topic, null, null, null, value, null));
    }

}
