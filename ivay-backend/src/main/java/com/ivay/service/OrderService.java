package com.ivay.service;

import java.util.List;

import com.ivay.dtos.orderdto.OrderResponseDto;
import com.ivay.dtos.orderdto.create.CreateOrderRequestDto;
import com.ivay.dtos.orderdto.update.UpdateOrderDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;

/**
 * Service interface for managing orders.
 *
 * Provides methods to:
 * - retrieve all orders or by user
 * - retrieve, create, update, and delete a single order
 * - fetch items belonging to a specific order
 *
 * @since 1.0.0
 */
public interface OrderService {

    /**
     * Retrieves all orders in the system.
     *
     * @return a list of {@link OrderResponseDto} representing all orders
     */
    List<OrderResponseDto> getAllOrders();

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId the identifier of the user
     * @return a list of {@link OrderResponseDto} for the given user
     */
    List<OrderResponseDto> getOrdersByUserId(Long userId);

    /**
     * Retrieves a single order by its identifier.
     *
     * @param orderId the identifier of the order to retrieve
     * @return the {@link OrderResponseDto} for the given order
     */
    OrderResponseDto getOrderById(Long orderId);

    /**
     * Creates a new order along with its items.
     *
     * @param createOrderRequestDto the DTO containing order and item data
     * @return the created {@link OrderResponseDto}
     */
    OrderResponseDto createOrder(CreateOrderRequestDto createOrderRequestDto);

    /**
     * Updates the specified fields of an existing order.
     *
     * @param orderId the identifier of the order to update
     * @param updateOrderDto the DTO containing the fields to update
     * @return the updated {@link OrderResponseDto}
     */
    OrderResponseDto updateOrder(Long orderId, UpdateOrderDto updateOrderDto);

    /**
     * Deletes an order by its identifier.
     *
     * @param orderId the identifier of the order to delete
     */
    void deleteOrder(Long orderId);

    /**
     * Retrieves all items associated with a specific order.
     *
     * @param orderId the identifier of the order
     * @return a list of {@link OrderItemResponseDto} for the order
     */
    List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId);

}
