package com.ivay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.dtos.orderdto.OrderResponseDto;
import com.ivay.dtos.orderdto.create.CreateOrderRequestDto;
import com.ivay.dtos.orderdto.update.UpdateOrderDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api") 
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class OrderController {

    private final OrderService orderService;

    // GET /api/orders
    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        ApiResponseDto<List<OrderResponseDto>> response = new ApiResponseDto<>("Orders fetched successfully", HttpStatus.OK.value(), orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/users/{userId}/orders
    @GetMapping(value = "/users/{userId}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
        ApiResponseDto<List<OrderResponseDto>> response = new ApiResponseDto<>("User's orders fetched successfully", HttpStatus.OK.value(), orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/orders/{orderId}
    @GetMapping(value = "/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> getOrderById(@PathVariable Long orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        ApiResponseDto<OrderResponseDto> response = new ApiResponseDto<>("Order fetched successfully", HttpStatus.OK.value(), order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/orders/{orderId}/items
    @GetMapping(value = "/orders/{orderId}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderItemResponseDto>>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemResponseDto> items = orderService.getOrderItemsByOrderId(orderId);
        ApiResponseDto<List<OrderItemResponseDto>> response = new ApiResponseDto<>("Order items fetched successfully", HttpStatus.OK.value(), items);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /api/orders
    @PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> createOrder(@Valid @RequestBody CreateOrderRequestDto createOrderRequestDto) {
        OrderResponseDto createdOrder = orderService.createOrder(createOrderRequestDto);
        ApiResponseDto<OrderResponseDto> response = new ApiResponseDto<>("Order created successfully", HttpStatus.CREATED.value(), createdOrder);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/orders/{orderId} (For limited updates)
    @PutMapping(value = "/orders/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> updateOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderDto updateOrderDto) {
        OrderResponseDto updatedOrder = orderService.updateOrder(orderId, updateOrderDto);
        ApiResponseDto<OrderResponseDto> response = new ApiResponseDto<>("Order updated successfully", HttpStatus.OK.value(), updatedOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE /api/orders/{orderId}
    @DeleteMapping(value = "/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
