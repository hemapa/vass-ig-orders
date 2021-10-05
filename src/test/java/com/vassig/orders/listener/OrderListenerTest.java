package com.vassig.orders.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vassig.orders.dto.OrderRequest;
import com.vassig.orders.dto.ProductRequest;
import com.vassig.orders.service.OrderService;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class OrderListenerTest {

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @InjectMocks
    OrderListener orderListener;

    @Mock
    OrderService orderService;

    public static final String CREATE_TOPIC = "create-topic";

    private KafkaTemplate<Long, OrderRequest> kafkaTemplate;

    ObjectMapper objectMapper;

    OrderRequest orderRequest = new OrderRequest();

    @BeforeEach
    void createOrder() {
        objectMapper= new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        kafkaTemplate = new KafkaTemplate<>(producerFactory(embeddedKafkaBroker.getBrokersAsString(), new LongSerializer()));

        List<ProductRequest> products = new ArrayList<>();
        products.add(new ProductRequest(20L, 1, 12.5F));

        orderRequest.setOrderDate(LocalDate.now());
        orderRequest.setCustomerId(1L);
        orderRequest.setExpectedShipmentDate(LocalDate.now());
        orderRequest.setProducts(products);
    }

    @Test
    void givenEmbeddedKafkaBroker_whenSendFromProducer_thenMessageReceived() {
        doNothing().when(orderService).createOrder(orderRequest);
        send(CREATE_TOPIC, orderRequest);
        orderListener.createOrder(orderRequest);
    }

    @Test
    void givenEmbeddedKafkaBroker_whenSendInvalidMessageFromProducer_thenExceptionReceived() {
        doThrow(RuntimeException.class).when(orderService).createOrder(orderRequest);
        send(CREATE_TOPIC, orderRequest);
        assertThrows(RuntimeException.class, () -> orderListener.createOrder(orderRequest));

    }

    public void send(String topic, OrderRequest payload) {
        kafkaTemplate.send(topic, payload);
    }

    private <K, V> ProducerFactory<K, V> producerFactory(String bootstrapServers, Serializer<K> keySerializer) {
        Map<String, Object> properties = Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        DefaultKafkaProducerFactory<K, V> defaultKafkaProducerFactory = new DefaultKafkaProducerFactory<>(properties);
        defaultKafkaProducerFactory.setKeySerializer(keySerializer);
        defaultKafkaProducerFactory.setValueSerializer(new JsonSerializer<>(objectMapper));
        return defaultKafkaProducerFactory;
    }
}