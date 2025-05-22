package com.ivay.dtos.productdto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for paginated product listings.
 *
 * Contains the list of products for the current page and
 * pagination metadata.
 *
 * Fields:
 * - content: list of products on this page
 * - page: current page index (0-based)
 * - size: number of items per page
 * - totalElements: total number of products available
 * - totalPages: total number of pages available
 * - hasNext: whether there is a subsequent page
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class PaginatedProductResponseDto {

    /**
     * List of products for the current page.
     */
    private List<ProductResponseDto> content;

    /**
     * Current page index (0-based).
     */
    private int page;

    /**
     * Number of items per page.
     */
    private int size;

    /**
     * Total number of elements across all pages.
     */
    private long totalElements;

    /**
     * Total number of pages available.
     */
    private int totalPages;

    /**
     * Indicates if there is a next page available.
     */
    private boolean hasNext;
}
