package com.ivay.dtos.orderdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderRequestDto {
    private Long userId;
    private LocalDateTime billDate;
    private String paymentMethod;
    private BigDecimal globalDiscount;
    private BigDecimal totalAmount;
    private BigDecimal totalAmountDiscounted;
    private Integer tax;
}
