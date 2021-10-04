package com.vassig.orders.listener;

import com.vassig.orders.dto.OrderRequest;
import com.vassig.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderListener {

    private final OrderService orderService;

    @KafkaListener(topics = "create-order",containerFactory = "")
    public void createOrder(OrderRequest orderRequest) {
        log.debug("Order to create {}",orderRequest.toString());
        orderService.createOrder(orderRequest);
    }
}
