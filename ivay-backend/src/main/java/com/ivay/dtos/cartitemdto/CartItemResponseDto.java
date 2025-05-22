package com.ivay.dtos.cartitemdto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing an item in a shopping cart.
 *
 * Contains all fields returned by the API when fetching cart item information:
 * id, userId, productId, and quantity.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class CartItemResponseDto {

    /**
     * Unique identifier of the cart item.
     */
    private Long id;

    /**
     * Identifier of the user who owns the cart.
     */
    private Long userId;

    /**
     * Identifier of the product in the cart.
     */
    private Long productId;

    /**
     * Quantity of the product in the cart.
     */
    private Integer quantity;
}
