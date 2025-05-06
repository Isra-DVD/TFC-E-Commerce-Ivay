package com.ivay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.dtos.orderdto.OrderResponseDto;
import com.ivay.dtos.orderdto.create.CreateOrderRequestDto;
import com.ivay.dtos.orderdto.update.UpdateOrderDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.service.OrderService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
@Tag(name = "Order", description = "Endpoints for managing orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(
        summary     = "Fetch all orders",
        description = "Retrieve a list of all orders from the database",
        tags        = { "Order" }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Orders fetched successfully",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes      = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(
                    name  = "OrdersList",
                    value = """
                        {
                          "timestamp": "2025-05-06T17:00:00.000Z",
                          "message": "Orders fetched successfully",
                          "code": 200,
                          "data": [
                            { "orderId": 1, "userId": 3, "total": 2400.00, "status": "CONFIRMED" },
                            { "orderId": 2, "userId": 4, "total":  800.00, "status": "PENDING" }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description  = "Internal server error",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        ApiResponseDto<List<OrderResponseDto>> response =
            new ApiResponseDto<>("Orders fetched successfully", HttpStatus.OK.value(), orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
        summary     = "Fetch orders by user ID",
        description = "Retrieve all orders placed by a specific user",
        tags        = { "Order" }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "User's orders fetched successfully",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes      = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(
                    name  = "UserOrders",
                    value = """
                        {
                          "timestamp": "2025-05-06T17:02:00.000Z",
                          "message": "User's orders fetched successfully",
                          "code": 200,
                          "data": [
                            { "orderId": 1, "userId": 3, "total": 2400.00, "status": "CONFIRMED" }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "User not found",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/users/{userId}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getOrdersByUserId(
        @Parameter(description = "Identifier of the user", required = true)
        @PathVariable Long userId
    ) {
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
        ApiResponseDto<List<OrderResponseDto>> response =
            new ApiResponseDto<>("User's orders fetched successfully", HttpStatus.OK.value(), orders);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
        summary     = "Fetch an order by ID",
        description = "Retrieve a single order using its ID",
        tags        = { "Order" }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Order fetched successfully",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes      = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(
                    name  = "OrderDetail",
                    value = """
                        {
                          "timestamp": "2025-05-06T17:04:00.000Z",
                          "message": "Order fetched successfully",
                          "code": 200,
                          "data": { "orderId": 1, "userId": 3, "total": 2400.00, "status": "CONFIRMED" }
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Order not found",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/orders/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> getOrderById(
        @Parameter(description = "Identifier of the order", required = true)
        @PathVariable Long orderId
    ) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        ApiResponseDto<OrderResponseDto> response =
            new ApiResponseDto<>("Order fetched successfully", HttpStatus.OK.value(), order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
        summary     = "Fetch order items by order ID",
        description = "Retrieve all items belonging to a specific order",
        tags        = { "Order" }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Order items fetched successfully",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes      = { OrderItemResponseDto.class }
                ),
                examples = @ExampleObject(
                    name  = "OrderItemsList",
                    value = """
                        {
                          "timestamp": "2025-05-06T17:06:00.000Z",
                          "message": "Order items fetched successfully",
                          "code": 200,
                          "data": [
                            { "orderItemId": 10, "orderId": 1, "productId": 1, "quantity": 2, "unitPrice": 1200.00 }
                          ]
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Order not found",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/orders/{orderId}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderItemResponseDto>>> getOrderItemsByOrderId(
        @Parameter(description = "Identifier of the order", required = true)
        @PathVariable Long orderId
    ) {
        List<OrderItemResponseDto> items = orderService.getOrderItemsByOrderId(orderId);
        ApiResponseDto<List<OrderItemResponseDto>> response =
            new ApiResponseDto<>("Order items fetched successfully", HttpStatus.OK.value(), items);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
        summary     = "Create a new order",
        description = "Place a new order in the system",
        tags        = { "Order" }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Order created successfully",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes      = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(
                    name  = "OrderCreated",
                    value = """
                        {
                          "timestamp": "2025-05-06T17:08:00.000Z",
                          "message": "Order created successfully",
                          "code": 201,
                          "data": { "orderId": 3, "userId": 5, "total": 1500.00, "status": "PENDING" }
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Invalid input",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> createOrder(
        @Parameter(description = "Order creation payload", required = true, schema = @Schema(implementation = CreateOrderRequestDto.class))
        @Valid @RequestBody CreateOrderRequestDto createOrderRequestDto
    ) {
        OrderResponseDto createdOrder = orderService.createOrder(createOrderRequestDto);
        ApiResponseDto<OrderResponseDto> response =
            new ApiResponseDto<>("Order created successfully", HttpStatus.CREATED.value(), createdOrder);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
        summary     = "Update an existing order",
        description = "Modify an existing order's details using its ID",
        tags        = { "Order" }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Order updated successfully",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes      = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(
                    name  = "OrderUpdated",
                    value = """
                        {
                          "timestamp": "2025-05-06T17:10:00.000Z",
                          "message": "Order updated successfully",
                          "code": 200,
                          "data": { "orderId": 1, "userId": 3, "total": 2400.00, "status": "SHIPPED" }
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Invalid input",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Order not found",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PutMapping(value = "/orders/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> updateOrder(
        @Parameter(description = "Identifier of the order", required = true)
        @PathVariable Long orderId,
        @Parameter(description = "Order update payload", required = true, schema = @Schema(implementation = UpdateOrderDto.class))
        @Valid @RequestBody UpdateOrderDto updateOrderDto
    ) {
        OrderResponseDto updatedOrder = orderService.updateOrder(orderId, updateOrderDto);
        ApiResponseDto<OrderResponseDto> response =
            new ApiResponseDto<>("Order updated successfully", HttpStatus.OK.value(), updatedOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
        summary     = "Delete an order by ID",
        description = "Remove an order from the database using its ID",
        tags        = { "Order" }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description  = "Order deleted successfully",
            content      = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Order not found",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @DeleteMapping(value = "/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(
        @Parameter(description = "Identifier of the order", required = true)
        @PathVariable Long orderId
    ) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
