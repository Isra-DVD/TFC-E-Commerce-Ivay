package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * JPA entity representing an item within an order.
 *
 * Maps to the "order_items" table and contains:
 * - id: primary key identifier of the order item
 * - order: reference to the parent Order
 * - product: reference to the Product being ordered
 * - quantity: number of units of the product
 * - discount: discount applied to this item
 * - price: unit price before discount
 * - totalPrice: total price after applying discount and quantity
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    /**
     * Primary key, auto-generated identifier of the order item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The order to which this item belongs.
     * Many order items can belong to one order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * The product included in this order item.
     * Many order items can refer to one product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Quantity of the product in this order item.
     */
    private Integer quantity;

    /**
     * Discount applied to this order item.
     * Stores up to 3 digits and 2 decimal places.
     */
    @Column(precision = 3, scale = 2)
    private BigDecimal discount;

    /**
     * Unit price of the product before discount.
     * Stores up to 10 digits and 2 decimal places.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Total price for this item after applying discount and quantity.
     * Stores up to 10 digits and 2 decimal places.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;
}
