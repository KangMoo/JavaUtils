/**
 * @author kangmoo Heo
 */
package com.github.kangmoo.utils.kafka;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.header.Header;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author kangmoo Heo
 */
public class UKafkaProducerAgent<K, V> {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private UKafkaProducer<K, V> producer;

    public UKafkaProducerAgent(UKafkaProducer<K, V> producer) {
        this.producer = producer;
    }

    public Future<RecordMetadata> send(String topic, Integer partition, Long timestamp, K key, V value) {
        try {
            return executor.submit(() -> producer.send(topic, partition, timestamp, key, value)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Future<RecordMetadata> send(String topic, Integer partition, K key, V value, Iterable<Header> headers) {
        try {
            return executor.submit(() -> producer.send(topic, partition, key, value, headers)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Future<RecordMetadata> send(String topic, Integer partition, K key, V value) {
        try {
            return executor.submit(() -> producer.send(topic, partition, key, value)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Future<RecordMetadata> send(String topic, K key, V value) {
        try {
            return executor.submit(() -> producer.send(topic, key, value)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Future<RecordMetadata> send(String topic, V value) {
        try {
            return executor.submit(() -> producer.send(topic, value)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initTransactions() {
        try {
            executor.submit(() -> producer.initTransactions()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void beginTransaction() throws ProducerFencedException {
        try {
            executor.submit(() -> producer.beginTransaction()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets, String consumerGroupId) throws ProducerFencedException {
        try {
            executor.submit(() -> producer.sendOffsetsToTransaction(offsets, consumerGroupId)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitTransaction() throws ProducerFencedException {
        try {
            executor.submit(() -> producer.commitTransaction()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void abortTransaction() throws ProducerFencedException {
        try {
            executor.submit(() -> producer.abortTransaction()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Future<RecordMetadata> send(ProducerRecord<K, V> record) {
        try {
            return executor.submit(() -> producer.send(record)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Future<RecordMetadata> send(ProducerRecord<K, V> record, Callback callback) {
        try {
            return executor.submit(() -> producer.send(record, callback)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void flush() {
        try {
            executor.submit(() -> producer.flush()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<PartitionInfo> partitionsFor(String topic) {
        try {
            return executor.submit(() -> producer.partitionsFor(topic)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<MetricName, ? extends Metric> metrics() {
        try {
            return executor.submit(() -> producer.metrics()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            executor.submit(() -> producer.close()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    public void close(Duration timeout) {
        try {
            executor.submit(() -> producer.close(timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    @Deprecated
    public void close(long timeoutMs) {
        try {
            executor.submit(() -> producer.close(Duration.ofMillis(timeoutMs))).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }
}
