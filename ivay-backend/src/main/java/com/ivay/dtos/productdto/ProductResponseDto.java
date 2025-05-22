package com.ivay.dtos.productdto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO representing a Product.
 *
 * Contains all fields returned by the API when fetching product information:
 * id, name, description, price, stock, discount, imageUrl, categoryId, and supplierId.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class ProductResponseDto {

    /**
     * Unique identifier of the product.
     */
    private Long id;

    /**
     * Name of the product.
     */
    private String name;

    /**
     * Detailed description of the product.
     */
    private String description;

    /**
     * Sale price of the product.
     */
    private BigDecimal price;

    /**
     * Available stock quantity.
     */
    private Integer stock;

    /**
     * Discount applied to the product price.
     */
    private BigDecimal discount;

    /**
     * URL pointing to the product image.
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
