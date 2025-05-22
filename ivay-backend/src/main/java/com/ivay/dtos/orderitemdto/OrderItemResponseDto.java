package com.ivay.dtos.orderitemdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO representing an item within an order.
 *
 * Contains all fields returned by the API when fetching order item information:
 * id, orderId, productId, quantity, discount, price, and totalPrice.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class OrderItemResponseDto {

    /**
     * Unique identifier of the order item.
     */
    private Long id;

    /**
     * Identifier of the parent order.
     */
    private Long orderId;

    /**
     * Identifier of the product in this order item.
     */
    private Long productId;

    /**
     * Quantity of the product ordered.
     */
    private Integer quantity;

    /**
     * Discount applied to this item.
     */
    private BigDecimal discount;

    /**
     * Unit price of the product before discount.
     */
    private BigDecimal price;

    /**
     * Total price for this item after applying discount and quantity.
     */
    private BigDecimal totalPrice;
}
