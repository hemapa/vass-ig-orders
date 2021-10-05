package com.vassig.orders.config;

import com.vassig.orders.dto.OrderRequest;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;

@Configuration
public class ConsumerConfig {

    @Value("${kafka.order.server}")
    String orderServer;

    @Value("${kafka.order.concurrency}")
    Integer orderConcurrency;

    @Value("${kafka.order.group-id}")
    String orderGroupId;

    @Value("${kafka.poll-timeout}")
    Long pollTimeout;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, OrderRequest> orderListenerContainerFactory() {
        return createContainerFactory(orderServer,
                jsonDeserializerForClass(Long.class),
                jsonDeserializerForClass(OrderRequest.class),
                orderGroupId,
                orderConcurrency);
    }

    public <K,V> ConsumerFactory<K, V> consumerFactory(String server,
                                                 Deserializer<K> keyDeserializer,
                                                 Deserializer<V> valueDeserializer,
                                                 String group) {
        Map<String,Object> config = new HashMap<>();
        config.put(BOOTSTRAP_SERVERS_CONFIG, server);
        config.put(GROUP_ID_CONFIG, group);
        return new DefaultKafkaConsumerFactory<>(config,keyDeserializer,valueDeserializer);
    }

    private <K, V> ConcurrentKafkaListenerContainerFactory<K, V> createContainerFactory(String server,
                                                                                        Deserializer<K> keyDeserializer,
                                                                                        Deserializer<V> valueDeserializer,
                                                                                        String group,
                                                                                        Integer concurrency) {
        ConcurrentKafkaListenerContainerFactory<K, V> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(server,keyDeserializer,valueDeserializer,group));
        factory.setConcurrency(orderConcurrency);
        factory.getContainerProperties().setPollTimeout(pollTimeout);
        return factory;
    }

    private <T> JsonDeserializer<T> jsonDeserializerForClass(Class<T> deserializationClass) {
        JsonDeserializer<T> keyDeserializer = new JsonDeserializer<>(deserializationClass);
        keyDeserializer.addTrustedPackages("*");
        keyDeserializer.setUseTypeMapperForKey(true);
        return keyDeserializer;
    }
}
