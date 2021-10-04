package com.vassig.orders.config;

import com.vassig.orders.dto.OrderRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka.order.server}")
    String orderServer;

    @Value("${kafka.order.concurrency}")
    Integer orderConcurrency;

    @Value("${kafka.poll-timeout}")
    Long pollTimeout;

    public ConsumerFactory<Long, OrderRequest> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, orderServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, Long.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, OrderRequest> kafkaListenerContainerFactory() {
        return createContainerFactory();
    }

    private <K, V> ConcurrentKafkaListenerContainerFactory<K, V> createContainerFactory(String server,
                                                                                        Deserializer<K> keyDeserializer,
                                                                                        Deserializer<V> valueDeserializer,
                                                                                        String group,
                                                                                        Integer concurrency,
                                                                                        Map config) {
        ConcurrentKafkaListenerContainerFactory<Long, OrderRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(orderConcurrency);
        factory.setMessageConverter(new StringJsonMessageConverter());
        factory.getContainerProperties().setPollTimeout(pollTimeout);
        return factory;
    }
}
