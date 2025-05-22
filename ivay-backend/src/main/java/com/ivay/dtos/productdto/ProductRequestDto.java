package com.ivay.dtos.productdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for creating or updating a Product.
 *
 * Contains all necessary fields to define a product:
 * name, description, price, stock, discount, image URL,
 * category ID, and supplier ID.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class ProductRequestDto {

    /**
     * The name of the product.
     */
    private String name;

    /**
     * A detailed description of the product.
     */
    private String description;

    /**
     * The sale price of the product.
     */
    private BigDecimal price;

    /**
     * The available stock quantity.
     */
    private Integer stock;

    /**
     * Discount applied to the product price.
     */
    private BigDecimal discount;

    /**
     * URL of the product image.
     */
    private String imageUrl;

    /**
     * Identifier of the category to which this product belongs.
     */
    private Long categoryId;

    /**
     * Identifier of the supplier providing this product.
     */
    private Long supplierId;
}
