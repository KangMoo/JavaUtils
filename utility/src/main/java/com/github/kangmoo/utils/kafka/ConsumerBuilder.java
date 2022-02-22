/**
 * @author kangmoo Heo
 */
package com.github.kangmoo.utils.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class ConsumerBuilder<K, V> {
    private Properties properties = new Properties();

    public ConsumerBuilder(String bootstrapServers, String groupId) {
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    }

    public void withProperty(Object key, Object value) {
        properties.put(key, value);
    }

    public UKafkaConsumer<K, V> build(){
        return new UKafkaConsumer<>(properties);
    }

}