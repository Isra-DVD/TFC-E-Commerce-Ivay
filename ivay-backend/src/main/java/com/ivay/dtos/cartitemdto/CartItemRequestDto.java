package com.ivay.dtos.cartitemdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemRequestDto {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
