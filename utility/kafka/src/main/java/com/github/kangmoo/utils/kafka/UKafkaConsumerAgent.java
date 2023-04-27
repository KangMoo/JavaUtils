package com.github.kangmoo.utils.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author kangmoo Heo
 */
public class UKafkaConsumerAgent<K, V> {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private UKafkaConsumer<K, V> consumer;

    public UKafkaConsumerAgent(UKafkaConsumer<K, V> consumer) {
        this.consumer = consumer;
    }

    public void subscribe(String... topics) {
        try {
            executor.submit(() -> consumer.subscribe(topics)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<TopicPartition> assignment() {
        try {
            return executor.submit(() -> consumer.assignment()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> subscription() {
        try {
            return executor.submit(() -> consumer.subscription()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(Collection<String> topics, ConsumerRebalanceListener listener) {
        try {
            executor.submit(() -> consumer.subscribe(topics, listener)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(Collection<String> topics) {
        try {
            executor.submit(() -> consumer.subscribe(topics)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(Pattern pattern, ConsumerRebalanceListener listener) {
        try {
            executor.submit(() -> consumer.subscribe(pattern, listener)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(Pattern pattern) {
        try {
            executor.submit(() -> consumer.subscribe(pattern)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void unsubscribe() {
        try {
            executor.submit(() -> consumer.unsubscribe()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void assign(Collection<TopicPartition> partitions) {
        try {
            executor.submit(() -> consumer.assign(partitions)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    public ConsumerRecords<K, V> poll(long timeoutMs) {
        try {
            return executor.submit(() -> consumer.poll(timeoutMs)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ConsumerRecords<K, V> poll(Duration timeout) {
        try {
            return executor.submit(() -> consumer.poll(timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitSync() {
        try {
            executor.submit(() -> consumer.commitSync()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitSync(Duration timeout) {
        try {
            executor.submit(() -> consumer.commitSync(timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitSync(Map<TopicPartition, OffsetAndMetadata> offsets) {
        try {
            executor.submit(() -> consumer.commitSync(offsets)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitSync(Map<TopicPartition, OffsetAndMetadata> offsets, Duration timeout) {
        try {
            executor.submit(() -> consumer.commitSync(offsets, timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitAsync() {
        try {
            executor.submit(() -> consumer.commitAsync()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitAsync(OffsetCommitCallback callback) {
        try {
            executor.submit(() -> consumer.commitAsync(callback)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitAsync(Map<TopicPartition, OffsetAndMetadata> offsets, OffsetCommitCallback callback) {
        try {
            executor.submit(() -> consumer.commitAsync(offsets, callback)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void seek(TopicPartition partition, long offset) {
        try {
            executor.submit(() -> consumer.seek(partition, offset)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void seek(TopicPartition partition, OffsetAndMetadata offsetAndMetadata) {
        try {
            executor.submit(() -> consumer.seek(partition, offsetAndMetadata)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void seekToBeginning(Collection<TopicPartition> partitions) {
        try {
            executor.submit(() -> consumer.seekToBeginning(partitions)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void seekToEnd(Collection<TopicPartition> partitions) {
        try {
            executor.submit(() -> consumer.seekToEnd(partitions)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long position(TopicPartition partition) {
        try {
            return executor.submit(() -> consumer.position(partition)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long position(TopicPartition partition, Duration timeout) {
        try {
            return executor.submit(() -> consumer.position(partition, timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public OffsetAndMetadata committed(TopicPartition partition) {
        try {
            return executor.submit(() -> consumer.committed(partition)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public OffsetAndMetadata committed(TopicPartition partition, Duration timeout) {
        try {
            return executor.submit(() -> consumer.committed(partition, timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<MetricName, ? extends Metric> metrics() {
        try {
            return executor.submit(() -> consumer.metrics()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<PartitionInfo> partitionsFor(String topic) {
        try {
            return executor.submit(() -> consumer.partitionsFor(topic)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<PartitionInfo> partitionsFor(String topic, Duration timeout) {
        try {
            return executor.submit(() -> consumer.partitionsFor(topic, timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<PartitionInfo>> listTopics() {
        try {
            return executor.submit(() -> consumer.listTopics()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<PartitionInfo>> listTopics(Duration timeout) {
        try {
            return executor.submit(() -> consumer.listTopics(timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void pause(Collection<TopicPartition> partitions) {
        try {
            executor.submit(() -> consumer.pause(partitions)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resume(Collection<TopicPartition> partitions) {
        try {
            executor.submit(() -> consumer.resume(partitions)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Set<TopicPartition> paused() {
        try {
            return executor.submit(() -> consumer.paused()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes(Map<TopicPartition, Long> timestampsToSearch) {
        try {
            return executor.submit(() -> consumer.offsetsForTimes(timestampsToSearch)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes(Map<TopicPartition, Long> timestampsToSearch, Duration timeout) {
        try {
            return executor.submit(() -> consumer.offsetsForTimes(timestampsToSearch, timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<TopicPartition, Long> beginningOffsets(Collection<TopicPartition> partitions) {
        try {
            return executor.submit(() -> consumer.beginningOffsets(partitions)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<TopicPartition, Long> beginningOffsets(Collection<TopicPartition> partitions, Duration timeout) {
        try {
            return executor.submit(() -> consumer.beginningOffsets(partitions, timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<TopicPartition, Long> endOffsets(Collection<TopicPartition> partitions) {
        try {
            return executor.submit(() -> consumer.endOffsets(partitions)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<TopicPartition, Long> endOffsets(Collection<TopicPartition> partitions, Duration timeout) {
        try {
            return executor.submit(() -> consumer.endOffsets(partitions, timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            executor.submit(() -> consumer.close()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    @Deprecated
    public void close(long timeoutMs) {
        try {
            executor.submit(() -> consumer.close(Duration.ofMillis(timeoutMs))).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    public void close(Duration timeout) {
        try {
            executor.submit(() -> consumer.close(timeout)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }

    public void wakeup() {
        try {
            executor.submit(() -> consumer.wakeup()).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}