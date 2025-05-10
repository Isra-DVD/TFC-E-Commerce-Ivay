package com.ivay.dtos.cartitemdto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartItemResponseDto {
    private Long id;  
    private Long userId;
    private Long productId;
    private Integer quantity;
}
