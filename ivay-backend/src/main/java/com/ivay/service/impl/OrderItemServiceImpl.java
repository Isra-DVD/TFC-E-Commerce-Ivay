package com.ivay.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.entity.OrderItem;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.OrderItemMapper;
import com.ivay.repository.OrderItemRepository;
import com.ivay.repository.OrderRepository;
import com.ivay.repository.ProductRepository;
import com.ivay.service.OrderItemService;

import java.util.List;

/**
 * Service implementation for managing order items.
 *
 * Provides methods to retrieve a single order item by its identifier,
 * as well as lists of order items filtered by order or product.
 * Each operation logs its activity and validates the existence of required entities.
 *
 * All public methods are executed within a transactional context.
 *
 * @since 1.0.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemMapper orderItemMapper;

    private static final String ORDER_ITEM_NOT_FOUND = "OrderItem with id %d not found";
    private static final String ORDER_NOT_FOUND      = "Order with id %d not found (for retrieving items)";
    private static final String PRODUCT_NOT_FOUND    = "Product with id %d not found (for retrieving items)";

    /**
     * Retrieves an OrderItem by its identifier, throwing if not found.
     *
     * @param orderItemId the identifier of the order item
     * @return the found OrderItem entity
     * @throws ResourceNotFoundException if no order item exists with the given id
     */
    private OrderItem validateAndGetOrderItem(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ORDER_ITEM_NOT_FOUND, orderItemId)));
    }

    /**
     * Verifies that an Order with the given id exists.
     *
     * @param orderId the identifier of the order to check
     * @throws ResourceNotFoundException if no order exists with the given id
     */
    private void validateOrderExists(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException(
                    String.format(ORDER_NOT_FOUND, orderId));
        }
    }

    /**
     * Verifies that a Product with the given id exists.
     *
     * @param productId the identifier of the product to check
     * @throws ResourceNotFoundException if no product exists with the given id
     */
    private void validateProductExists(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(
                    String.format(PRODUCT_NOT_FOUND, productId));
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fetches a single order item by its identifier.
     *
     * @param orderItemId the id of the order item to retrieve
     * @return the corresponding OrderItemResponseDto
     */
    @Override
    public OrderItemResponseDto getOrderItemById(Long orderItemId) {
        log.info("Fetching order item with id: {}", orderItemId);
        OrderItem orderItem = validateAndGetOrderItem(orderItemId);
        return orderItemMapper.toOrderItemResponse(orderItem);
    }

    /**
     * {@inheritDoc}
     *
     * Retrieves all order items associated with a specific order.
     *
     * @param orderId the id of the order whose items to fetch
     * @return list of OrderItemResponseDto for that order
     */
    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
        log.info("Fetching order items for order id: {}", orderId);
        validateOrderExists(orderId);
        List<OrderItem> items = orderItemRepository.findByOrder_Id(orderId);
        return items.stream()
                    .map(orderItemMapper::toOrderItemResponse)
                    .toList();
    }

    /**
     * {@inheritDoc}
     *
     * Retrieves all order items that include a specific product.
     *
     * @param productId the id of the product whose order items to fetch
     * @return list of OrderItemResponseDto containing that product
     */
    @Override
    public List<OrderItemResponseDto> getOrderItemsByProductId(Long productId) {
        log.info("Fetching order items for product id: {}", productId);
        validateProductExists(productId);
        List<OrderItem> items = orderItemRepository.findByProduct_Id(productId);
        return items.stream()
                    .map(orderItemMapper::toOrderItemResponse)
                    .toList();
    }
}
