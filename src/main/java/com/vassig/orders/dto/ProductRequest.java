package com.vassig.orders.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {

    private Long productId;
    private Integer quantity;
    private Float price;

}
