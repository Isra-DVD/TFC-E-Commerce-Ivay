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

/**
 * REST controller for managing order items.
 *
 * Exposes an endpoint to retrieve a single order item by its ID.
 * Responses are wrapped in {@link ApiResponseDto} or return an error
 * payload {@link ApiError}.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
@Tag(name = "OrderItem", description = "Endpoints for managing order items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    /**
     * Retrieve a single order item by its ID.
     *
     * @param orderItemId identifier of the order item
     * @return HTTP 200 with the {@link OrderItemResponseDto} in ApiResponseDto
     */
    @Operation(
        summary     = "Fetch an order item by ID",
        description = "Retrieve a single order item using its ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Order item fetched successfully",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { OrderItemResponseDto.class }
                ),
                examples = @ExampleObject(
                    name  = "OrderItemDetail",
                    value = """
                        {
                          "timestamp": "2025-05-06T16:00:00.000Z",
                          "message": "Order item fetched successfully",
                          "code": 200,
                          "data": {
                            "id": 42,
                            "orderId": 7,
                            "productId": 15,
                            "quantity": 3,
                            "discount": 0.10,
                            "price": 19.99,
                            "totalPrice": 53.97
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
                          "timestamp": "2025-05-06T16:01:00.000Z",
                          "status": 404,
                          "message": "Order item with id 42 not found",
                          "errors": ["Order item with id 42 not found"]
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
        return ResponseEntity.ok(response);
    }
}
