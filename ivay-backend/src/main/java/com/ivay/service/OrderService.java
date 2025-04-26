package com.ivay.service;

import java.util.List;

import com.ivay.dtos.orderdto.OrderResponseDto;
import com.ivay.dtos.orderdto.create.CreateOrderRequestDto;
import com.ivay.dtos.orderdto.update.UpdateOrderDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;

public interface OrderService {
    List<OrderResponseDto> getAllOrders();
    List<OrderResponseDto> getOrdersByUserId(Long userId);
    OrderResponseDto getOrderById(Long orderId);
    OrderResponseDto createOrder(CreateOrderRequestDto createOrderRequestDto);
    OrderResponseDto updateOrder(Long orderId, UpdateOrderDto updateOrderDto); 
    void deleteOrder(Long orderId);
    List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId); 
}
