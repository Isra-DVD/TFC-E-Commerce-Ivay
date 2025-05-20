package com.ivay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.service.OrderItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/"})
@Tag(name = "OrderItem", description = "Endpoints for managing order items")
public class OrderItemController {

	private final OrderItemService orderItemService;

	@Operation(
			summary     = "Fetch an order item by ID",
			description = "Retrieve a single order item from the database using its ID",
			tags        = { "OrderItem" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Order item fetched successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { OrderItemResponseDto.class }
								),
						examples = @ExampleObject(
								name  = "OrderItemResponse",
								value = """
										{
										  "timestamp": "2025-05-06T16:00:00.123456",
										  "message": "Order item fetched successfully",
										  "code": 200,
										  "data": {
										    "orderItemId": 42,
										    "orderId": 7,
										    "productId": 15,
										    "quantity": 3,
										    "unitPrice": 19.99
										  }
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "404",
				description  = "Order item not found",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(implementation = ApiError.class),
						examples = @ExampleObject(
								name  = "NotFoundError",
								value = """
										{
										  "timestamp": "2025-05-06T16:01:00.654321",
										  "status": 404,
										  "error": "Resource Not Found",
										  "message": "Order item with id 42 not found"
										}
										"""
								)
						)
				)
	})
	@GetMapping(value = "/order-items/{orderItemId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<OrderItemResponseDto>> getOrderItemById(
			@Parameter(description = "Identifier of the order item", required = true)
			@PathVariable Long orderItemId
			) {
		OrderItemResponseDto orderItem = orderItemService.getOrderItemById(orderItemId);
		ApiResponseDto<OrderItemResponseDto> response =
				new ApiResponseDto<>("Order item fetched successfully", HttpStatus.OK.value(), orderItem);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}