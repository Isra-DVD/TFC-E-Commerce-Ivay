package com.ivay.service.impl;

import jakarta.persistence.EntityManager; 
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
import com.ivay.entity.User;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.OrderItemMapper;
import com.ivay.mappers.OrderMapper;
import com.ivay.repository.OrderItemRepository;
import com.ivay.repository.OrderRepository;
import com.ivay.repository.ProductRepository;
import com.ivay.repository.UserRepository;
import com.ivay.service.OrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional 
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    private static final String ORDER_NOT_FOUND = "Order with id %d not found";
    private static final String USER_NOT_FOUND = "User with id %d not found (for order creation)";
    private static final String PRODUCT_NOT_FOUND = "Product with id %d not found (for order item creation)";

    private Order validateAndGetOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, orderId)));
    }

    private User validateAndGetUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, userId)));
    }

     private Product validateAndGetProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, productId)));
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user id: {}", userId);
        validateAndGetUser(userId);
        return orderRepository.findByUser_Id(userId)
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        log.info("Fetching order with id: {}", orderId);
        Order order = validateAndGetOrder(orderId);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional 
    public OrderResponseDto createOrder(CreateOrderRequestDto createOrderRequestDto) {
        log.info("Attempting to create order for user id: {}", createOrderRequestDto.getUserId());

        User user = validateAndGetUser(createOrderRequestDto.getUserId());

        Order order = new Order();
        order.setUser(user);
        order.setBillDate(LocalDateTime.now()); 
        order.setPaymentMethod(createOrderRequestDto.getPaymentMethod()); 
        order.setGlobalDiscount(createOrderRequestDto.getGlobalDiscount() != null ? createOrderRequestDto.getGlobalDiscount() : BigDecimal.ZERO);
        order.setOrderItems(new ArrayList<>()); 


        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> createdItems = new ArrayList<>();

        for (CreateOrderItemRequestDto itemDto : createOrderRequestDto.getItems()) {
            Product product = validateAndGetProduct(itemDto.getProductId());
            int quantity = itemDto.getQuantity();

            if (product.getStock() == null || product.getStock() < quantity) {
                log.error("Insufficient stock for product id {} ({}). Requested: {}, Available: {}",
                          product.getId(), product.getName(), quantity, product.getStock());
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); 
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice()); 
            orderItem.setDiscount(product.getDiscount() != null ? product.getDiscount() : BigDecimal.ZERO); 

            BigDecimal itemTotal = product.getPrice()
                                       .multiply(BigDecimal.valueOf(quantity))
                                       .multiply(BigDecimal.ONE.subtract(orderItem.getDiscount()))
                                       .setScale(2, RoundingMode.HALF_UP);
            orderItem.setTotalPrice(itemTotal);

            totalAmount = totalAmount.add(itemTotal);

            product.setStock(product.getStock() - quantity);
            productRepository.save(product); 

            order.getOrderItems().add(orderItem);
            createdItems.add(orderItem); 
        }

        BigDecimal totalAmountDiscounted = totalAmount.multiply(BigDecimal.ONE.subtract(order.getGlobalDiscount()))
                                                  .setScale(2, RoundingMode.HALF_UP);
        order.setTotalAmount(totalAmount); 
        order.setTotalAmountDiscounted(totalAmountDiscounted);

        // TODO: Calculate tax based on items/total/rules
        order.setTax(0); 

        Order finalSavedOrder = orderRepository.save(order);

        log.info("Successfully created order with id: {}", finalSavedOrder.getId());
        return orderMapper.toOrderResponse(finalSavedOrder);
    }

    @Override
    public OrderResponseDto updateOrder(Long orderId, UpdateOrderDto updateOrderDto) {
        log.info("Attempting to update order id: {}", orderId);
        Order order = validateAndGetOrder(orderId);

        boolean updated = false;
        if (updateOrderDto.getPaymentMethod() != null && !updateOrderDto.getPaymentMethod().equals(order.getPaymentMethod())) {
            log.debug("Updating payment method for order {}", orderId);
            order.setPaymentMethod(updateOrderDto.getPaymentMethod());
            updated = true;
        }
         if (updateOrderDto.getGlobalDiscount() != null && updateOrderDto.getGlobalDiscount().compareTo(order.getGlobalDiscount()) != 0) {
            log.warn("Updating global discount for order {}. Recalculation of totalAmountDiscounted might be needed!", orderId);
            order.setGlobalDiscount(updateOrderDto.getGlobalDiscount());
             BigDecimal totalAmountDiscounted = order.getTotalAmount().multiply(BigDecimal.ONE.subtract(order.getGlobalDiscount()))
                                                  .setScale(2, RoundingMode.HALF_UP);
             order.setTotalAmountDiscounted(totalAmountDiscounted);
            updated = true;
        }

        if (updated) {
            Order savedOrder = orderRepository.save(order);
             log.info("Successfully updated order id: {}", orderId);
            return orderMapper.toOrderResponse(savedOrder);
        } else {
             log.info("No updatable fields provided for order id: {}", orderId);
             return orderMapper.toOrderResponse(order);
        }
    }

    @Override
    public void deleteOrder(Long orderId) {
        log.warn("Attempting to permanently delete order with id: {}. This is generally discouraged.", orderId);
        Order order = validateAndGetOrder(orderId);

        orderRepository.delete(order);
        log.warn("Permanently deleted order with id: {}", orderId);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
         log.info("Fetching items for order id {} via OrderService", orderId);
         Order order = validateAndGetOrder(orderId);
         return order.getOrderItems().stream()
                 .map(orderItemMapper::toOrderItemResponse)
                 .toList();
    }
}
