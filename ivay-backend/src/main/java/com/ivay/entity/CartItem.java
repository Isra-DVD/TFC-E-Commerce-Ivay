package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity representing an item in a user's shopping cart.
 *
 * Maps to the "cart_items" table and includes:
 * - id: primary key
 * - user: the owner of the cart item
 * - product: the product being added to the cart
 * - quantity: number of units of the product
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "cart_items")
public class CartItem {

    /**
     * Primary key, auto-generated identifier of the cart item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the user who owns this cart item.
     * Many cart items can belong to one user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * Reference to the product added to the cart.
     * Many cart items can refer to one product.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * Quantity of the product in the cart.
     */
    private Integer quantity;
}
