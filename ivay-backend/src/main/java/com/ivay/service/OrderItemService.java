package com.ivay.service;

import java.util.List;

import com.ivay.dtos.orderitemdto.OrderItemResponseDto;

public interface OrderItemService {

    OrderItemResponseDto getOrderItemById(Long orderItemId);

    List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId);

    List<OrderItemResponseDto> getOrderItemsByProductId(Long productId);
}
