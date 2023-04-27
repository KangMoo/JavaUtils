/**
 * @author kangmoo Heo
 */
package com.github.kangmoo.utils.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerBuilder<K, V> {
    private final Properties properties = new Properties();

    public ProducerBuilder(String bootstrapServers) {
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    }

    public ProducerBuilder<K, V> withProperties(Object key, Object value) {
        properties.put(key, value);
        return this;
    }

    public UKafkaProducer<K, V> build() {
        return new UKafkaProducer<>(properties);
    }

}