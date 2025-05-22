package com.ivay.dtos.orderdto.update;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating specific fields of an existing order.
 *
 * Contains optional fields that can be modified:
 * paymentMethod and globalDiscount.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class UpdateOrderDto {

    /**
     * New payment method for the order (e.g. "CREDIT_CARD", "PAYPAL").
     */
    private String paymentMethod;

    /**
     * New discount to apply to the entire order before tax.
     */
    private BigDecimal globalDiscount;
}
