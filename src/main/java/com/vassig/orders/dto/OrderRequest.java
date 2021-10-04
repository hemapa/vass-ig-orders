package com.vassig.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class OrderRequest {

    private Long customerId;
    private LocalDate orderDate;
    private LocalDate expectedShipmentDate;
    private List<ProductRequest> products;

}
