package com.ivay.service;

import java.util.List;

import com.ivay.dtos.cartitemdto.CartItemRequestDto;
import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.dtos.cartitemdto.UpdateCartItemQuantityDto;

public interface CartItemService {
    CartItemResponseDto getCartItemById(Long cartItemId);

    List<CartItemResponseDto> getCartItemsByUserId(Long userId);

    CartItemResponseDto addOrUpdateCartItem(CartItemRequestDto cartItemRequestDto);

    CartItemResponseDto updateCartItemQuantity(Long cartItemId, UpdateCartItemQuantityDto updateDto);

    void deleteCartItem(Long cartItemId);

    void clearUserCart(Long userId);
}
