package com.ivay.dtos.orderdto.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.ivay.dtos.orderitemdto.create.CreateOrderItemRequestDto;

@Data
@NoArgsConstructor
public class CreateOrderRequestDto {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    private String paymentMethod;

    private BigDecimal globalDiscount;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid 
    private List<CreateOrderItemRequestDto> items;
}
