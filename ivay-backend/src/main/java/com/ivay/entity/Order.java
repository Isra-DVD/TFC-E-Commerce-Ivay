package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JPA entity representing an Order.
 *
 * Maps to the "orders" table and contains:
 * - id: primary key identifier
 * - user: the owner of the order
 * - billDate: date and time when the order was billed
 * - paymentMethod: payment method used for the order
 * - globalDiscount: discount applied to the entire order
 * - totalAmount: total amount before discount
 * - totalAmountDiscounted: total amount after discount
 * - tax: tax percentage applied to the order
 * - orderItems: list of items belonging to this order
 *
 * CascadeType.ALL is used on orderItems to propagate persistence operations,
 * and orphanRemoval=true to delete items removed from the list.
 *
 * @since 1.0.0
 */
@Data
@ToString(exclude = "orderItems")
@EqualsAndHashCode(exclude = "orderItems")
@Entity
@Table(name = "orders")
public class Order {

    /**
     * Primary key, auto-generated identifier of the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the user who placed this order.
     * Many orders can belong to one user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * Date and time when the order was billed.
     */
    private LocalDateTime billDate;

    /**
     * Payment method used for the order.
     * Maximum length is 50 characters.
     */
    @Column(length = 50)
    private String paymentMethod;

    /**
     * Discount applied to the entire order before tax.
     * Stores up to 3 digits and 2 decimal places.
     */
    @Column(precision = 3, scale = 2)
    private BigDecimal globalDiscount;

    /**
     * Total amount of the order before applying discounts.
     * Stores up to 10 digits and 2 decimal places.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * Total amount of the order after applying discounts.
     * Stores up to 10 digits and 2 decimal places.
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmountDiscounted;

    /**
     * Tax percentage applied to the order (e.g. 21 for 21% VAT).
     */
    private Integer tax;

    /**
     * Items included in this order.
     * CascadeType.ALL propagates all operations to orderItems.
     * orphanRemoval=true deletes items removed from this list.
     */
    @OneToMany(
        mappedBy = "order",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OrderItem> orderItems;
}
