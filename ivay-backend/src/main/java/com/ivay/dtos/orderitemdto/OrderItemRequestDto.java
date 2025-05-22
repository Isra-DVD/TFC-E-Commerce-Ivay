package com.ivay.dtos.orderitemdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating or updating an order item.
 *
 * Contains all necessary fields to represent an item within an order:
 * - orderId: identifier of the parent order
 * - productId: identifier of the product
 * - quantity: number of units ordered
 * - discount: discount applied to this item
 * - price: unit price before discount
 * - totalPrice: total price after applying discount and quantity
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class OrderItemRequestDto {

    /**
     * Identifier of the order to which this item belongs.
     */
    private Long orderId;

    /**
     * Identifier of the product being ordered.
     */
    private Long productId;

    /**
     * Quantity of the product in this order item.
     */
    private Integer quantity;

    /**
     * Discount applied to this order item.
     */
    private BigDecimal discount;

    /**
     * Unit price of the product before discount.
     */
    private BigDecimal price;

    /**
     * Total price for this item after applying discount and multiplying by quantity.
     */
    private BigDecimal totalPrice;
}
