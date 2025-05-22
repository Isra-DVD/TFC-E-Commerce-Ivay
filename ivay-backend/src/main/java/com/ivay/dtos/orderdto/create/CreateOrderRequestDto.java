package com.ivay.dtos.orderdto.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.ivay.dtos.orderitemdto.create.CreateOrderItemRequestDto;

/**
 * Request DTO for creating a new order.
 *
 * This DTO contains:
 * - userId: identifier of the user placing the order (required)
 * - paymentMethod: chosen method of payment
 * - globalDiscount: discount applied to the entire order
 * - items: list of order items (must contain at least one valid item)
 *
 * Validation constraints ensure userId is not null and items list is not empty.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class CreateOrderRequestDto {

    /**
     * Identifier of the user placing the order.
     * Must not be null.
     */
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    /**
     * Payment method for the order (for example "CREDIT_CARD" or "PAYPAL").
     */
    private String paymentMethod;

    /**
     * Discount to apply to the entire order before tax.
     */
    private BigDecimal globalDiscount;

    /**
     * List of items included in the order.
     * Must contain at least one item and each item is validated.
     */
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<CreateOrderItemRequestDto> items;
}
