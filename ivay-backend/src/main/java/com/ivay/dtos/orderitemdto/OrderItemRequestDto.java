package com.ivay.dtos.orderitemdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemRequestDto {
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
