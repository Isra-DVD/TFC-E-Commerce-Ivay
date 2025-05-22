package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * JPA entity representing a Product.
 *
 * Maps to the "products" table and contains:
 * - id: primary key identifier
 * - name: product name, required and max 100 characters
 * - description: detailed product description
 * - price: sale price with two decimal precision
 * - stock: available inventory count
 * - discount: discount percentage with two decimal precision
 * - imageUrl: URL to the product image
 * - category: reference to the product's category
 * - supplier: reference to the product's supplier
 * - orderItems: list of order items associated with this product
 * - cartItems: list of cart items associated with this product
 *
 * @since 1.0.0
 */
@Data
@ToString(exclude = {"orderItems", "cartItems"})
@EqualsAndHashCode(exclude = {"orderItems", "cartItems"})
@Entity
@Table(name = "products")
public class Product {

    /**
     * Primary key, auto-generated identifier of the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the product.
     * Cannot be null and maximum length is 100 characters.
     */
    @Column(length = 100, nullable = false)
    private String name;

    /**
     * Detailed description of the product.
     * Stored as TEXT in the database.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Sale price of the product.
     * Stores up to 10 digits and 2 decimal places.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Available stock quantity.
     */
    private Integer stock;

    /**
     * Discount applied to the product price.
     * Stores up to 3 digits and 2 decimal places.
     */
    @Column(precision = 3, scale = 2)
    private BigDecimal discount;

    /**
     * URL pointing to the product image.
     * Maximum length is 255 characters.
     */
    @Column(length = 255)
    private String imageUrl;

    /**
     * Category to which this product belongs.
     * Many products can belong to one category.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Supplier providing this product.
     * Many products can refer to one supplier.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    /**
     * Order items that include this product.
     * One product can appear in many order items.
     */
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    /**
     * Cart items that include this product.
     * One product can appear in many cart items.
     */
    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;
}
