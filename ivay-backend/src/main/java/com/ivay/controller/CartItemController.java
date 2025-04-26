package com.ivay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.dtos.cartitemdto.CartItemRequestDto;
import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.dtos.cartitemdto.UpdateCartItemQuantityDto;
import com.ivay.service.CartItemService;

import java.util.List;

@RestController
@RequestMapping("/api") 
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    // GET /api/cart-items/{cartItemId}
    @GetMapping(value = "/cart-items/{cartItemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartItemResponseDto>> getCartItemById(@PathVariable Long cartItemId) {
        CartItemResponseDto cartItem = cartItemService.getCartItemById(cartItemId);
        ApiResponseDto<CartItemResponseDto> response = new ApiResponseDto<>("Cart item fetched successfully", HttpStatus.OK.value(), cartItem);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/users/{userId}/cart-items  (Get all items for a specific user)
    @GetMapping(value = "/users/{userId}/cart-items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<CartItemResponseDto>>> getCartItemsByUserId(@PathVariable Long userId) {
        List<CartItemResponseDto> cartItems = cartItemService.getCartItemsByUserId(userId);
        ApiResponseDto<List<CartItemResponseDto>> response = new ApiResponseDto<>("User's cart items fetched successfully", HttpStatus.OK.value(), cartItems);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /api/cart-items (Add item to cart or update quantity if exists)
    @PostMapping(value = "/cart-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartItemResponseDto>> addOrUpdateCartItem(@Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        CartItemResponseDto savedCartItem = cartItemService.addOrUpdateCartItem(cartItemRequestDto);
        ApiResponseDto<CartItemResponseDto> response = new ApiResponseDto<>("Cart item added/updated successfully", HttpStatus.OK.value(), savedCartItem);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // PATCH /api/cart-items/{cartItemId}/quantity (Specific endpoint for quantity update is often preferred)
    @PatchMapping(value = "/cart-items/{cartItemId}/quantity", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CartItemResponseDto>> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemQuantityDto updateDto) {
        CartItemResponseDto updatedCartItem = cartItemService.updateCartItemQuantity(cartItemId, updateDto);
        ApiResponseDto<CartItemResponseDto> response = new ApiResponseDto<>("Cart item quantity updated successfully", HttpStatus.OK.value(), updatedCartItem);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE /api/cart-items/{cartItemId} (Remove one specific item)
    @DeleteMapping(value = "/cart-items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        cartItemService.deleteCartItem(cartItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // DELETE /api/users/{userId}/cart-items (Clear entire cart for user)
    @DeleteMapping(value = "/users/{userId}/cart-items")
    public ResponseEntity<Void> clearUserCart(@PathVariable Long userId) {
        cartItemService.clearUserCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
