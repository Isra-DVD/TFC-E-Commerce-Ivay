package com.ivay.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ivay.dtos.orderdto.OrderResponseDto;
import com.ivay.dtos.orderdto.create.CreateOrderRequestDto;
import com.ivay.dtos.orderdto.update.UpdateOrderDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.dtos.orderitemdto.create.CreateOrderItemRequestDto;
import com.ivay.entity.Order;
import com.ivay.entity.OrderItem;
import com.ivay.entity.Product;
import com.ivay.entity.UserEntity;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.OrderItemMapper;
import com.ivay.mappers.OrderMapper;
import com.ivay.repository.OrderRepository;
import com.ivay.repository.ProductRepository;
import com.ivay.repository.UserRepository;
import com.ivay.service.OrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for managing orders.
 *
 * Implements methods to create, retrieve, update and delete orders,
 * as well as to fetch order items for a given order.
 * Validates existence of users, products and orders, checks stock levels,
 * applies discounts and calculates totals.
 *
 * All operations are executed within a transactional context.
 *
 * @since 1.0.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    private static final String ORDER_NOT_FOUND   = "Order with id %d not found";
    private static final String USER_NOT_FOUND    = "User with id %d not found (for order creation)";
    private static final String PRODUCT_NOT_FOUND = "Product with id %d not found (for order item creation)";

    /**
     * Retrieves an Order entity by its identifier or throws if not found.
     *
     * @param orderId the id of the order to validate
     * @return the Order entity
     * @throws ResourceNotFoundException if no order exists with that id
     */
    private Order validateAndGetOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(ORDER_NOT_FOUND, orderId)));
    }

    /**
     * Retrieves a UserEntity by its identifier or throws if not found.
     *
     * @param userId the id of the user to validate
     * @return the UserEntity
     * @throws ResourceNotFoundException if no user exists with that id
     */
    private UserEntity validateAndGetUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(USER_NOT_FOUND, userId)));
    }

    /**
     * Retrieves a Product by its identifier or throws if not found.
     *
     * @param productId the id of the product to validate
     * @return the Product entity
     * @throws ResourceNotFoundException if no product exists with that id
     */
    private Product validateAndGetProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(PRODUCT_NOT_FOUND, productId)));
    }

    /**
     * {@inheritDoc}
     *
     * Fetches all orders in the system.
     *
     * @return list of OrderResponseDto representing every order
     */
    @Override
    public List<OrderResponseDto> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream()
            .map(orderMapper::toOrderResponse)
            .toList();
    }

    /**
     * {@inheritDoc}
     *
     * Fetches all orders placed by a given user.
     *
     * @param userId the id of the user
     * @return list of OrderResponseDto for the specified user
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user id: {}", userId);
        validateAndGetUser(userId);
        return orderRepository.findByUser_Id(userId).stream()
            .map(orderMapper::toOrderResponse)
            .toList();
    }

    /**
     * {@inheritDoc}
     *
     * Fetches a single order by its identifier.
     *
     * @param orderId the id of the order
     * @return the OrderResponseDto for the found order
     * @throws ResourceNotFoundException if no order exists with that id
     */
    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        log.info("Fetching order with id: {}", orderId);
        Order order = validateAndGetOrder(orderId);
        return orderMapper.toOrderResponse(order);
    }

    /**
     * {@inheritDoc}
     *
     * Creates a new order with associated items.
     * Validates user and product existence, checks stock,
     * calculates item and order totals, updates product stock,
     * and persists the order and its items.
     *
     * @param createOrderRequestDto the DTO containing order and item data
     * @return the created OrderResponseDto
     * @throws ResourceNotFoundException if the user or any product is not found
     * @throws IllegalStateException if any item quantity exceeds available stock
     */
    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto createOrderRequestDto) {
        log.info("Attempting to create order for user id: {}", createOrderRequestDto.getUserId());

        UserEntity user = validateAndGetUser(createOrderRequestDto.getUserId());

        Order order = new Order();
        order.setUser(user);
        order.setBillDate(LocalDateTime.now());
        order.setPaymentMethod(createOrderRequestDto.getPaymentMethod());
        order.setGlobalDiscount(createOrderRequestDto.getGlobalDiscount() != null
            ? createOrderRequestDto.getGlobalDiscount() : BigDecimal.ZERO);
        order.setOrderItems(new ArrayList<>());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CreateOrderItemRequestDto itemDto : createOrderRequestDto.getItems()) {
            Product product = validateAndGetProduct(itemDto.getProductId());
            int quantity = itemDto.getQuantity();

            if (product.getStock() == null || product.getStock() < quantity) {
                log.error("Insufficient stock for product id {}. Requested: {}, Available: {}",
                    product.getId(), quantity, product.getStock());
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());
            orderItem.setDiscount(product.getDiscount() != null
                ? product.getDiscount() : BigDecimal.ZERO);

            BigDecimal itemTotal = product.getPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .multiply(BigDecimal.ONE.subtract(orderItem.getDiscount()))
                .setScale(2, RoundingMode.HALF_UP);
            orderItem.setTotalPrice(itemTotal);

            totalAmount = totalAmount.add(itemTotal);

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            order.getOrderItems().add(orderItem);
        }

        BigDecimal totalAmountDiscounted = totalAmount
            .multiply(BigDecimal.ONE.subtract(order.getGlobalDiscount()))
            .setScale(2, RoundingMode.HALF_UP);
        order.setTotalAmount(totalAmount);
        order.setTotalAmountDiscounted(totalAmountDiscounted);

        order.setTax(0); // Tax calculation to be implemented

        Order savedOrder = orderRepository.save(order);
        log.info("Successfully created order with id: {}", savedOrder.getId());
        return orderMapper.toOrderResponse(savedOrder);
    }

    /**
     * {@inheritDoc}
     *
     * Updates mutable fields of an existing order such as payment method
     * and global discount, recalculating totals if needed.
     *
     * @param orderId the id of the order to update
     * @param updateOrderDto the DTO containing fields to update
     * @return the updated OrderResponseDto
     * @throws ResourceNotFoundException if the order does not exist
     */
    @Override
    public OrderResponseDto updateOrder(Long orderId, UpdateOrderDto updateOrderDto) {
        log.info("Attempting to update order id: {}", orderId);
        Order order = validateAndGetOrder(orderId);

        boolean updated = false;
        if (updateOrderDto.getPaymentMethod() != null
            && !updateOrderDto.getPaymentMethod().equals(order.getPaymentMethod())) {
            order.setPaymentMethod(updateOrderDto.getPaymentMethod());
            updated = true;
        }
        if (updateOrderDto.getGlobalDiscount() != null
            && updateOrderDto.getGlobalDiscount().compareTo(order.getGlobalDiscount()) != 0) {
            order.setGlobalDiscount(updateOrderDto.getGlobalDiscount());
            BigDecimal discounted = order.getTotalAmount()
                .multiply(BigDecimal.ONE.subtract(order.getGlobalDiscount()))
                .setScale(2, RoundingMode.HALF_UP);
            order.setTotalAmountDiscounted(discounted);
            updated = true;
        }

        if (updated) {
            Order saved = orderRepository.save(order);
            log.info("Successfully updated order id: {}", orderId);
            return orderMapper.toOrderResponse(saved);
        } else {
            log.info("No changes applied to order id: {}", orderId);
            return orderMapper.toOrderResponse(order);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Deletes an order by its identifier.
     *
     * @param orderId the id of the order to delete
     * @throws ResourceNotFoundException if no order exists with that id
     */
    @Override
    public void deleteOrder(Long orderId) {
        log.warn("Deleting order id: {}", orderId);
        Order order = validateAndGetOrder(orderId);
        orderRepository.delete(order);
        log.warn("Deleted order id: {}", orderId);
    }

    /**
     * {@inheritDoc}
     *
     * Retrieves the list of order items for a given order.
     *
     * @param orderId the id of the order
     * @return list of OrderItemResponseDto for that order
     * @throws ResourceNotFoundException if the order does not exist
     */
    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
        log.info("Fetching items for order id: {}", orderId);
        Order order = validateAndGetOrder(orderId);
        return order.getOrderItems().stream()
            .map(orderItemMapper::toOrderItemResponse)
            .toList();
    }
}
