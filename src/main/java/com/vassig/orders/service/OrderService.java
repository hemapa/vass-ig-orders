package com.vassig.orders.service;

import com.vassig.orders.dto.OrderRequest;
import com.vassig.orders.mapper.OrderMapper;
import com.vassig.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public void createOrder(OrderRequest orderRequest){
        Optional.of(orderMapper.fromRequest(orderRequest))
                .map(orderRepository::save)
                .orElseThrow(RuntimeException::new);
    }

}
