/**
 * @author kangmoo Heo
 */
package com.github.kangmoo.utils.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class UKafkaConsumer<K, V> extends KafkaConsumer<K, V> {
    public UKafkaConsumer(Map<String, Object> configs, Queue<V> queue) {
        super(configs);
    }

    public UKafkaConsumer(Map<String, Object> configs, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
        super(configs, keyDeserializer, valueDeserializer);
    }

    public UKafkaConsumer(Properties properties) {
        super(properties);
    }

    public UKafkaConsumer(Properties properties, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer) {
        super(properties, keyDeserializer, valueDeserializer);
    }

    public void subscribe(String... topics) {
        super.subscribe(Arrays.asList(topics));
    }

    @Override
    public Set<TopicPartition> assignment() {
        return super.assignment();
    }

    @Override
    public Set<String> subscription() {
        return super.subscription();
    }

    @Override
    public void subscribe(Collection<String> topics, ConsumerRebalanceListener listener) {
        super.subscribe(topics, listener);
    }

    @Override
    public void subscribe(Collection<String> topics) {
        super.subscribe(topics);
    }

    @Override
    public void subscribe(Pattern pattern, ConsumerRebalanceListener listener) {
        super.subscribe(pattern, listener);
    }

    @Override
    public void subscribe(Pattern pattern) {
        super.subscribe(pattern);
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void assign(Collection<TopicPartition> partitions) {
        super.assign(partitions);
    }

    @Override
    public ConsumerRecords<K, V> poll(long timeoutMs) {
        return super.poll(Duration.ofMillis(timeoutMs));
    }

    @Override
    public ConsumerRecords<K, V> poll(Duration timeout) {
        return super.poll(timeout);
    }

    @Override
    public void commitSync() {
        super.commitSync();
    }

    @Override
    public void commitSync(Duration timeout) {
        super.commitSync(timeout);
    }

    @Override
    public void commitSync(Map<TopicPartition, OffsetAndMetadata> offsets) {
        super.commitSync(offsets);
    }

    @Override
    public void commitSync(Map<TopicPartition, OffsetAndMetadata> offsets, Duration timeout) {
        super.commitSync(offsets, timeout);
    }

    @Override
    public void commitAsync() {
        super.commitAsync();
    }

    @Override
    public void commitAsync(OffsetCommitCallback callback) {
        super.commitAsync(callback);
    }

    @Override
    public void commitAsync(Map<TopicPartition, OffsetAndMetadata> offsets, OffsetCommitCallback callback) {
        super.commitAsync(offsets, callback);
    }

    @Override
    public void seek(TopicPartition partition, long offset) {
        super.seek(partition, offset);
    }

    @Override
    public void seek(TopicPartition partition, OffsetAndMetadata offsetAndMetadata) {
        super.seek(partition, offsetAndMetadata);
    }

    @Override
    public void seekToBeginning(Collection<TopicPartition> partitions) {
        super.seekToBeginning(partitions);
    }

    @Override
    public void seekToEnd(Collection<TopicPartition> partitions) {
        super.seekToEnd(partitions);
    }

    @Override
    public long position(TopicPartition partition) {
        return super.position(partition);
    }

    @Override
    public long position(TopicPartition partition, Duration timeout) {
        return super.position(partition, timeout);
    }

    @Override
    public OffsetAndMetadata committed(TopicPartition partition) {
        return super.committed(partition);
    }

    @Override
    public OffsetAndMetadata committed(TopicPartition partition, Duration timeout) {
        return super.committed(partition, timeout);
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return super.metrics();
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic) {
        return super.partitionsFor(topic);
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic, Duration timeout) {
        return super.partitionsFor(topic, timeout);
    }

    @Override
    public Map<String, List<PartitionInfo>> listTopics() {
        return super.listTopics();
    }

    @Override
    public Map<String, List<PartitionInfo>> listTopics(Duration timeout) {
        return super.listTopics(timeout);
    }

    @Override
    public void pause(Collection<TopicPartition> partitions) {
        super.pause(partitions);
    }

    @Override
    public void resume(Collection<TopicPartition> partitions) {
        super.resume(partitions);
    }

    @Override
    public Set<TopicPartition> paused() {
        return super.paused();
    }

    @Override
    public Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes(Map<TopicPartition, Long> timestampsToSearch) {
        return super.offsetsForTimes(timestampsToSearch);
    }

    @Override
    public Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes(Map<TopicPartition, Long> timestampsToSearch, Duration timeout) {
        return super.offsetsForTimes(timestampsToSearch, timeout);
    }

    @Override
    public Map<TopicPartition, Long> beginningOffsets(Collection<TopicPartition> partitions) {
        return super.beginningOffsets(partitions);
    }

    @Override
    public Map<TopicPartition, Long> beginningOffsets(Collection<TopicPartition> partitions, Duration timeout) {
        return super.beginningOffsets(partitions, timeout);
    }

    @Override
    public Map<TopicPartition, Long> endOffsets(Collection<TopicPartition> partitions) {
        return super.endOffsets(partitions);
    }

    @Override
    public Map<TopicPartition, Long> endOffsets(Collection<TopicPartition> partitions, Duration timeout) {
        return super.endOffsets(partitions, timeout);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void close(long timeout, TimeUnit timeUnit) {
        super.close(timeout, timeUnit);
    }

    @Override
    public void close(Duration timeout) {
        super.close(timeout);
    }

    @Override
    public void wakeup() {
        super.wakeup();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}