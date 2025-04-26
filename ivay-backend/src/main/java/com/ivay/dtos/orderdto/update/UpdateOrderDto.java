package com.ivay.dtos.orderdto.update;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class UpdateOrderDto {
    private String paymentMethod;
    private BigDecimal globalDiscount; 
}
