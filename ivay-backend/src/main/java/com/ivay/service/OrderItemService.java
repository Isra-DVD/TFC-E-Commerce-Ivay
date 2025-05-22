package com.ivay.service;

import java.util.List;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;

/**
 * Service interface for managing order items.
 *
 * Provides methods to:
 * - retrieve a single order item by its identifier
 * - retrieve all order items for a specific order
 * - retrieve all order items that include a specific product
 *
 * @since 1.0.0
 */
public interface OrderItemService {

    /**
     * Retrieves a single order item by its identifier.
     *
     * @param orderItemId the identifier of the order item to retrieve
     * @return the {@link OrderItemResponseDto} for the given order item
     */
    OrderItemResponseDto getOrderItemById(Long orderItemId);

    /**
     * Retrieves all order items associated with a specific order.
     *
     * @param orderId the identifier of the order
     * @return a list of {@link OrderItemResponseDto} for that order
     */
    List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId);

    /**
     * Retrieves all order items that include a specific product.
     *
     * @param productId the identifier of the product
     * @return a list of {@link OrderItemResponseDto} containing that product
     */
    List<OrderItemResponseDto> getOrderItemsByProductId(Long productId);
}
