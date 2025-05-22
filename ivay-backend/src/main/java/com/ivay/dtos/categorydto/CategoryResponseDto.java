package com.ivay.dtos.categorydto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing a category.
 *
 * Contains the identifier and name of the category
 * as returned by the API.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class CategoryResponseDto {

    /**
     * Unique identifier of the category.
     */
    private Long id;

    /**
     * Name of the category.
     */
    private String name;
}
