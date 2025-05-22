package com.ivay.dtos.orderdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO for creating or updating an Order.
 *
 * Contains all necessary fields to submit an order:
 * userId, billDate, paymentMethod, globalDiscount,
 * totalAmount, totalAmountDiscounted, and tax.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class OrderRequestDto {

    /**
     * Identifier of the user placing the order.
     */
    private Long userId;

    /**
     * Date and time when the order was billed.
     */
    private LocalDateTime billDate;

    /**
     * Payment method chosen for the order (e.g. "CREDIT_CARD", "PAYPAL").
     */
    private String paymentMethod;

    /**
     * Discount applied to the entire order before tax.
     */
    private BigDecimal globalDiscount;

    /**
     * Total amount of the order before applying discounts.
     */
    private BigDecimal totalAmount;

    /**
     * Total amount of the order after applying discounts.
     */
    private BigDecimal totalAmountDiscounted;

    /**
     * Tax percentage applied to the order (e.g. 21 for 21% VAT).
     */
    private Integer tax;
}
