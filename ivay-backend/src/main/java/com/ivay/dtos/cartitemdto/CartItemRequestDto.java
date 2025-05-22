package com.ivay.dtos.cartitemdto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a cart item.
 *
 * Contains the identifiers for the user and product,
 * as well as the desired quantity.
 *
 * Validation ensures all fields are provided.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class CartItemRequestDto {

    /**
     * Identifier of the user owning the cart.
     * Must not be null.
     */
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;

    /**
     * Identifier of the product to add or update.
     * Must not be null.
     */
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productId;

    /**
     * Desired quantity of the product in the cart.
     * Must not be null.
     */
    @NotNull(message = "La cantidad actualizada es obligatoria")
    private Integer quantity;
}
