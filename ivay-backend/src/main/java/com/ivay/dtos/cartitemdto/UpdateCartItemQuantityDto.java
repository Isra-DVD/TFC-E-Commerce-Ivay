package com.ivay.dtos.cartitemdto;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for updating the quantity of an existing cart item.
 *
 * Contains a single field representing the new quantity,
 * with validation constraints to ensure it is not null
 * and at least 1.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class UpdateCartItemQuantityDto {

    /**
     * The new quantity for the cart item.
     * Must not be null and must be at least 1.
     */
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
