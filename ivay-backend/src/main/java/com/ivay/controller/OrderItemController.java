package com.ivay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.service.OrderItemService;

@RestController
@RequestMapping("/api") 
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    // GET /api/order-items/{orderItemId}
    @GetMapping(value = "/order-items/{orderItemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<OrderItemResponseDto>> getOrderItemById(@PathVariable Long orderItemId) {
        OrderItemResponseDto orderItem = orderItemService.getOrderItemById(orderItemId);
        ApiResponseDto<OrderItemResponseDto> response = new ApiResponseDto<>("Order item fetched successfully", HttpStatus.OK.value(), orderItem);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
