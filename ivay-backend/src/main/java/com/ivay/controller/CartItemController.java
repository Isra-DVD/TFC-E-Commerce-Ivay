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
import com.ivay.dtos.cartitemdto.CartItemRequestDto;
import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.dtos.cartitemdto.UpdateCartItemQuantityDto;
import com.ivay.service.CartItemService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;

/**
 * REST controller for shopping cart item operations.
 *
 * Exposes endpoints to retrieve, add/update, patch quantity,
 * and delete cart items, as well as clear a user's entire cart.
 *
 * All responses are wrapped in {@link ApiResponseDto} or return an
 * error payload {@link ApiError}.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "http://localhost:3001",
	    "http://localhost:5173",
	    "http://localhost:5174",
	    "http://localhost:5678"
	})@Tag(name = "CartItem", description = "Endpoints for managing shopping cart items")
public class CartItemController {

    private final CartItemService cartItemService;

    /**
     * Retrieve a single cart item by its ID.
     *
     * @param cartItemId identifier of the cart item to fetch
     * @return HTTP 200 with the cart item in {@link ApiResponseDto}
     */
    @Operation(
        summary     = "Fetch a cart item by ID",
        description = "Retrieve a single cart item using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart item fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiResponseDto.class, subTypes = { CartItemResponseDto.class }),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:15:00.000Z",
                      "message": "Cart item fetched successfully",
                      "code": 200,
                      "data": {
                        "id": 10,
                        "userId": 5,
                        "productId": 3,
                        "quantity": 2
                      }
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Cart item not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/cart-items/{cartItemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or @cartItemServiceImpl.isOwner(#cartItemId, authentication.name)")
    public ResponseEntity<ApiResponseDto<CartItemResponseDto>> getCartItemById(
        @Parameter(description = "Identifier of the cart item", required = true)
        @PathVariable Long cartItemId
    ) {
        CartItemResponseDto cartItem = cartItemService.getCartItemById(cartItemId);
        ApiResponseDto<CartItemResponseDto> response =
            new ApiResponseDto<>("Cart item fetched successfully", HttpStatus.OK.value(), cartItem);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all cart items for a specific user.
     *
     * @param userId identifier of the user
     * @return HTTP 200 with list of cart items
     */
    @Operation(
        summary     = "Fetch all cart items for a user",
        description = "Retrieve all items in the shopping cart of a specific user"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User's cart items fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiResponseDto.class, subTypes = { CartItemResponseDto.class }),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:16:00.000Z",
                      "message": "User's cart items fetched successfully",
                      "code": 200,
                      "data": [
                        { "id": 10, "userId": 5, "productId": 3, "quantity": 2 },
                        { "id": 11, "userId": 5, "productId": 7, "quantity": 1 }
                      ]
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/users/{userId}/cart-items", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or #userId == @userEntityServiceImpl.getByUsername(authentication.name).id")
    public ResponseEntity<ApiResponseDto<List<CartItemResponseDto>>> getCartItemsByUserId(
        @Parameter(description = "Identifier of the user", required = true)
        @PathVariable Long userId
    ) {
        List<CartItemResponseDto> cartItems = cartItemService.getCartItemsByUserId(userId);
        ApiResponseDto<List<CartItemResponseDto>> response =
            new ApiResponseDto<>("User's cart items fetched successfully", HttpStatus.OK.value(), cartItems);
        return ResponseEntity.ok(response);
    }

    /**
     * Add a new cart item or update its quantity.
     *
     * @param cartItemRequestDto payload with userId, productId, and quantity
     * @return HTTP 200 with the created or updated cart item
     */
    @Operation(
        summary     = "Add or update a cart item",
        description = "Add a new product to the cart or update its quantity if it already exists"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart item added/updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiResponseDto.class, subTypes = { CartItemResponseDto.class }),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:17:00.000Z",
                      "message": "Cart item added/updated successfully",
                      "code": 200,
                      "data": { "id": 12, "userId": 5, "productId": 3, "quantity": 4 }
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "User or product not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping(value = "/cart-items",
                 consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or #cartItemRequestDto.userId == @userEntityServiceImpl.getByUsername(authentication.name).id")
    public ResponseEntity<ApiResponseDto<CartItemResponseDto>> addOrUpdateCartItem(
        @Parameter(description = "Cart item payload", required = true,
                   schema = @Schema(implementation = CartItemRequestDto.class))
        @Valid @RequestBody CartItemRequestDto cartItemRequestDto
    ) {
        CartItemResponseDto saved = cartItemService.addOrUpdateCartItem(cartItemRequestDto);
        ApiResponseDto<CartItemResponseDto> response =
            new ApiResponseDto<>("Cart item added/updated successfully", HttpStatus.OK.value(), saved);
        return ResponseEntity.ok(response);
    }

    /**
     * Update only the quantity of an existing cart item.
     *
     * @param cartItemId identifier of the cart item
     * @param updateDto  payload containing the new quantity
     * @return HTTP 200 with the updated cart item
     */
    @Operation(
        summary     = "Update quantity of a cart item",
        description = "Change only the quantity of an existing cart item"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart item quantity updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiResponseDto.class, subTypes = { CartItemResponseDto.class }),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:18:00.000Z",
                      "message": "Cart item quantity updated successfully",
                      "code": 200,
                      "data": { "id": 10, "userId": 5, "productId": 3, "quantity": 5 }
                    }
                    """)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Cart item not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PatchMapping(value = "/cart-items/{cartItemId}/quantity",
                  consumes = MediaType.APPLICATION_JSON_VALUE,
                  produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or @cartItemServiceImpl.isOwner(#cartItemId, authentication.name)")
    public ResponseEntity<ApiResponseDto<CartItemResponseDto>> updateCartItemQuantity(
        @Parameter(description = "Identifier of the cart item", required = true)
        @PathVariable Long cartItemId,
        @Parameter(description = "Quantity update payload", required = true,
                   schema = @Schema(implementation = UpdateCartItemQuantityDto.class))
        @Valid @RequestBody UpdateCartItemQuantityDto updateDto
    ) {
        CartItemResponseDto updated = cartItemService.updateCartItemQuantity(cartItemId, updateDto);
        ApiResponseDto<CartItemResponseDto> response =
            new ApiResponseDto<>("Cart item quantity updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a single cart item by ID.
     *
     * @param cartItemId identifier of the cart item to delete
     * @return HTTP 204 No Content
     */
    @Operation(
        summary     = "Delete a cart item",
        description = "Remove a single item from the cart by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cart item deleted successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cart item not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @DeleteMapping(value = "/cart-items/{cartItemId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or @cartItemServiceImpl.isOwner(#cartItemId, authentication.name)")
    public ResponseEntity<Void> deleteCartItem(
        @Parameter(description = "Identifier of the cart item", required = true)
        @PathVariable Long cartItemId
    ) {
        cartItemService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Clear all items in a user's cart.
     *
     * @param userId identifier of the user whose cart to clear
     * @return HTTP 204 No Content
     */
    @Operation(
        summary     = "Clear a user's cart",
        description = "Remove all items in the shopping cart of a specific user"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User's cart cleared successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @DeleteMapping(value = "/users/{userId}/cart-items")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or @userEntityServiceImpl.getByUsername(authentication.name).id == #userId")
    public ResponseEntity<Void> clearUserCart(
        @Parameter(description = "Identifier of the user", required = true)
        @PathVariable Long userId
    ) {
        cartItemService.clearUserCart(userId);
        return ResponseEntity.noContent().build();
    }
}
