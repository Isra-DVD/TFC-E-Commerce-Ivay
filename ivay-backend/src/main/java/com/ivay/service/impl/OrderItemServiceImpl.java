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
    private static final String ORDER_NOT_FOUND = "Order with id %d not found (for retrieving items)";
    private static final String PRODUCT_NOT_FOUND = "Product with id %d not found (for retrieving items)";


    private OrderItem validateAndGetOrderItem(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORDER_ITEM_NOT_FOUND, orderItemId)));
    }

     private void validateOrderExists(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
             throw new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
        }
    }
     private void validateProductExists(Long productId) {
        if (!productRepository.existsById(productId)) {
             throw new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, productId));
        }
    }


    @Override
    public OrderItemResponseDto getOrderItemById(Long orderItemId) {
        log.info("Fetching order item with id: {}", orderItemId);
        OrderItem orderItem = validateAndGetOrderItem(orderItemId);
        return orderItemMapper.toOrderItemResponse(orderItem);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
        log.info("Fetching order items for order id: {}", orderId);
        validateOrderExists(orderId); 
        List<OrderItem> items = orderItemRepository.findByOrder_Id(orderId);
        return items.stream()
                .map(orderItemMapper::toOrderItemResponse)
                .toList();
    }

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
