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
import org.apache.kafka.common.serialization.Serializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class UKafkaProducer<K, V> extends KafkaProducer<K, V> {
    public UKafkaProducer(Map configs) {
        super(configs);
    }

    public UKafkaProducer(Map configs, Serializer keySerializer, Serializer valueSerializer) {
        super(configs, keySerializer, valueSerializer);
    }

    public UKafkaProducer(Properties properties) {
        super(properties);
    }

    public UKafkaProducer(Properties properties, Serializer keySerializer, Serializer valueSerializer) {
        super(properties, keySerializer, valueSerializer);
    }

    public Future<RecordMetadata> send(String topic, V value){
        return this.send(new ProducerRecord<K, V>(topic, value));
    }

    @Override
    public void initTransactions() {
        super.initTransactions();
    }

    @Override
    public void beginTransaction() throws ProducerFencedException {
        super.beginTransaction();
    }

    @Override
    public void sendOffsetsToTransaction(Map offsets, String consumerGroupId) throws ProducerFencedException {
        super.sendOffsetsToTransaction(offsets, consumerGroupId);
    }

    @Override
    public void commitTransaction() throws ProducerFencedException {
        super.commitTransaction();
    }

    @Override
    public void abortTransaction() throws ProducerFencedException {
        super.abortTransaction();
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord record) {
        return super.send(record);
    }

    @Override
    public Future<RecordMetadata> send(ProducerRecord record, Callback callback) {
        return super.send(record, callback);
    }

    @Override
    public void flush() {
        super.flush();
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic) {
        return super.partitionsFor(topic);
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        return super.metrics();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void close(Duration timeout) {
        super.close(timeout);
    }

    @Override
    public void close(long timeout, TimeUnit unit) {
        super.close(timeout, unit);
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
