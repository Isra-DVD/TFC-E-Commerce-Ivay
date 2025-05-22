package com.ivay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

/**
 * REST controller for managing orders.
 *
 * Exposes endpoints to list, retrieve, create, update, and delete orders,
 * as well as to list order items for a given order.
 *
 * All responses are wrapped in {@link ApiResponseDto} or return an error
 * payload {@link ApiError}.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
@Tag(name = "Order", description = "Endpoints for managing orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Retrieve all orders.
     *
     * @return HTTP 200 with list of {@link OrderResponseDto}
     */
    @Operation(
        summary     = "Fetch all orders",
        description = "Retrieve a list of all orders in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Orders fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T17:00:00.000Z",
                      "message": "Orders fetched successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 1,
                          "userId": 3,
                          "billDate": "2025-05-06T10:00:00",
                          "paymentMethod": "CARD",
                          "globalDiscount": 0,
                          "totalAmount": 2400.00,
                          "totalAmountDiscounted": 2400.00,
                          "tax": 0
                        },
                        {
                          "id": 2,
                          "userId": 4,
                          "billDate": "2025-05-06T11:00:00",
                          "paymentMethod": "PAYPAL",
                          "globalDiscount": 0.10,
                          "totalAmount": 1000.00,
                          "totalAmountDiscounted": 900.00,
                          "tax": 0
                        }
                      ]
                    }
                    """
                )
            )
        )
    })
    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        ApiResponseDto<List<OrderResponseDto>> response =
            new ApiResponseDto<>("Orders fetched successfully", HttpStatus.OK.value(), orders);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all orders for a specific user.
     *
     * @param userId the user’s identifier
     * @return HTTP 200 with list of {@link OrderResponseDto}
     */
    @Operation(
        summary     = "Fetch orders by user ID",
        description = "Retrieve all orders placed by a specific user"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User's orders fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T17:02:00.000Z",
                      "message": "User's orders fetched successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 1,
                          "userId": 3,
                          "billDate": "2025-05-06T10:00:00",
                          "paymentMethod": "CARD",
                          "globalDiscount": 0,
                          "totalAmount": 2400.00,
                          "totalAmountDiscounted": 2400.00,
                          "tax": 0
                        }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(
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
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a single order by its ID.
     *
     * @param orderId the order’s identifier
     * @return HTTP 200 with {@link OrderResponseDto}
     */
    @Operation(
        summary     = "Fetch an order by ID",
        description = "Retrieve a single order using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T17:04:00.000Z",
                      "message": "Order fetched successfully",
                      "code": 200,
                      "data": {
                        "id": 1,
                        "userId": 3,
                        "billDate": "2025-05-06T10:00:00",
                        "paymentMethod": "CARD",
                        "globalDiscount": 0,
                        "totalAmount": 2400.00,
                        "totalAmountDiscounted": 2400.00,
                        "tax": 0
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(
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
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all items for a given order.
     *
     * @param orderId the order’s identifier
     * @return HTTP 200 with list of {@link OrderItemResponseDto}
     */
    @Operation(
        summary     = "Fetch order items by order ID",
        description = "Retrieve all items belonging to a specific order"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order items fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { OrderItemResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T17:06:00.000Z",
                      "message": "Order items fetched successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 10,
                          "orderId": 1,
                          "productId": 1,
                          "quantity": 2,
                          "discount": 0,
                          "price": 1200.00,
                          "totalPrice": 2400.00
                        }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(
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
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new order.
     *
     * Only the authenticated user matching userId in payload or an ADMIN/SUPERADMIN may create.
     *
     * @param createOrderRequestDto payload with userId, paymentMethod, discount, and items
     * @return HTTP 201 with created {@link OrderResponseDto}
     */
    @Operation(
        summary     = "Create a new order",
        description = "Place a new order in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Order created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T17:08:00.000Z",
                      "message": "Order created successfully",
                      "code": 201,
                      "data": {
                        "id": 3,
                        "userId": 5,
                        "billDate": "2025-05-06T12:00:00",
                        "paymentMethod": "CARD",
                        "globalDiscount": 0.05,
                        "totalAmount": 1500.00,
                        "totalAmountDiscounted": 1425.00,
                        "tax": 0
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping(
        value    = "/orders",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or #createOrderRequestDto.userId == @userEntityServiceImpl.getByUsername(authentication.name).id")
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> createOrder(
        @Parameter(description = "Order creation payload", required = true,
                   schema = @Schema(implementation = CreateOrderRequestDto.class))
        @Valid @RequestBody CreateOrderRequestDto createOrderRequestDto
    ) {
        OrderResponseDto created = orderService.createOrder(createOrderRequestDto);
        ApiResponseDto<OrderResponseDto> response =
            new ApiResponseDto<>("Order created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing order.
     *
     * @param orderId        identifier of the order to update
     * @param updateOrderDto payload containing fields to update
     * @return HTTP 200 with updated {@link OrderResponseDto}
     */
    @Operation(
        summary     = "Update an existing order",
        description = "Modify an existing order's details using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { OrderResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T17:10:00.000Z",
                      "message": "Order updated successfully",
                      "code": 200,
                      "data": {
                        "id": 1,
                        "userId": 3,
                        "billDate": "2025-05-06T10:00:00",
                        "paymentMethod": "PAYPAL",
                        "globalDiscount": 0.10,
                        "totalAmount": 2400.00,
                        "totalAmountDiscounted": 2160.00,
                        "tax": 0
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PutMapping(
        value    = "/orders/{orderId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> updateOrder(
        @Parameter(description = "Identifier of the order", required = true)
        @PathVariable Long orderId,
        @Parameter(description = "Order update payload", required = true,
                   schema = @Schema(implementation = UpdateOrderDto.class))
        @Valid @RequestBody UpdateOrderDto updateOrderDto
    ) {
        OrderResponseDto updated = orderService.updateOrder(orderId, updateOrderDto);
        ApiResponseDto<OrderResponseDto> response =
            new ApiResponseDto<>("Order updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an order by its ID.
     *
     * @param orderId identifier of the order to delete
     * @return HTTP 204 No Content
     */
    @Operation(
        summary     = "Delete an order by ID",
        description = "Remove an order from the system using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Order deleted successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Order not found",
            content = @Content(
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
        return ResponseEntity.noContent().build();
    }
}
