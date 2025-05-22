package com.ivay.dtos.orderitemdto.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating a new order item.
 *
 * This DTO contains:
 * - productId: identifier of the product to add (required)
 * - quantity: number of units to add (required, minimum 1)
 *
 * Validation constraints ensure both fields are provided and quantity is at least 1.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class CreateOrderItemRequestDto {

    /**
     * Identifier of the product to include in the order.
     * Must not be null.
     */
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    /**
     * Quantity of the product to order.
     * Must not be null and must be at least 1.
     */
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}
