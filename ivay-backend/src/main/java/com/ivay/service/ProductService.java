package com.ivay.service;

import java.util.List;

import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.dtos.productdto.PaginatedProductResponseDto;
import com.ivay.dtos.productdto.ProductRequestDto;
import com.ivay.dtos.productdto.ProductResponseDto;

/**
 * Service interface for managing products.
 *
 * Provides methods to:
 * - retrieve all products or a paginated subset
 * - search products by name
 * - perform CRUD operations on products
 * - retrieve associated order items and cart items for a product
 *
 * @since 1.0.0
 */
public interface ProductService {

    /**
     * Retrieves all products in the system.
     *
     * @return a list of {@link ProductResponseDto} representing all products
     */
    List<ProductResponseDto> getAllProducts();

    /**
     * Retrieves a page of products with pagination metadata.
     *
     * @param page zero-based page index
     * @param size number of items per page
     * @return a {@link PaginatedProductResponseDto} containing the page of products and metadata
     */
    PaginatedProductResponseDto getProductsPaginated(int page, int size);

    /**
     * Finds products whose name contains the given substring, case-insensitive.
     *
     * @param name substring to search for within product names
     * @return a list of matching {@link ProductResponseDto}
     */
    List<ProductResponseDto> findProductsByName(String name);

    /**
     * Retrieves a single product by its identifier.
     *
     * @param productId the identifier of the product to retrieve
     * @return the {@link ProductResponseDto} for the given product
     */
    ProductResponseDto getProductById(Long productId);

    /**
     * Creates a new product.
     *
     * @param productRequestDto the data for the new product
     * @return the created {@link ProductResponseDto}
     */
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    /**
     * Updates an existing product.
     *
     * @param productId the identifier of the product to update
     * @param productRequestDto the new data for the product
     * @return the updated {@link ProductResponseDto}
     */
    ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto);

    /**
     * Deletes a product by its identifier.
     *
     * @param productId the identifier of the product to delete
     */
    void deleteProduct(Long productId);

    /**
     * Retrieves all order items that include a specific product.
     *
     * @param productId the identifier of the product
     * @return a list of {@link OrderItemResponseDto} containing that product
     */
    List<OrderItemResponseDto> getOrderItemsByProductId(Long productId);

    /**
     * Retrieves all cart items that include a specific product.
     *
     * @param productId the identifier of the product
     * @return a list of {@link CartItemResponseDto} containing that product
     */
    List<CartItemResponseDto> getCartItemsByProductId(Long productId);
}
