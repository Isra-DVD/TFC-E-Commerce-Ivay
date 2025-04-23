package com.ivay.dtos.productdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private BigDecimal discount;
    private Long categoryId;
    private Long supplierId;
}
