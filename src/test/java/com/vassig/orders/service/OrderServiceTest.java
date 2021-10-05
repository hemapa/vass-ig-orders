package com.vassig.orders.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vassig.orders.dto.OrderRequest;
import com.vassig.orders.dto.ProductRequest;
import com.vassig.orders.mapper.OrderMapper;
import com.vassig.orders.mapper.OrderMapperImpl;
import com.vassig.orders.repository.OrderRepository;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {OrderService.class, OrderRepository.class, OrderMapperImpl.class})
@AutoConfigureDataMongo

class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    OrderRequest orderRequest = new OrderRequest();

    @BeforeEach
    void setup() {
        List<ProductRequest> products = new ArrayList<>();
        products.add(new ProductRequest(20L, 1, 12.5F));

        orderRequest.setOrderDate(LocalDate.now());
        orderRequest.setCustomerId(1L);
        orderRequest.setExpectedShipmentDate(LocalDate.now());
        orderRequest.setProducts(products);
    }

    @Test
    void createOrder() {

        assertTrue( orderRepository.findAll().size()==0);

        orderService.createOrder(orderRequest);

        assertTrue( orderRepository.findAll().size()>0);


    }
}