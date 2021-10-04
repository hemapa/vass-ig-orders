package com.vassig.orders.repository;

import com.vassig.orders.dto.ProductRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Order {

    @Id
    private Long id;
    private Long customerId;
    private LocalDate orderDate;
    private LocalDate expectedShipmentDate;
    private List<ProductRequest> products;

}
