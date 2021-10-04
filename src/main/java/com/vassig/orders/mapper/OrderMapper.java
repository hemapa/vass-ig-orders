package com.vassig.orders.mapper;

import com.vassig.orders.dto.OrderRequest;
import com.vassig.orders.repository.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring",injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderMapper {

    Optional<Order> fromRequest(OrderRequest request);
}
