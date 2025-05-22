package com.ivay.service;

import java.util.List;

import com.ivay.dtos.cartitemdto.CartItemRequestDto;
import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.dtos.cartitemdto.UpdateCartItemQuantityDto;

/**
 * Service interface for managing shopping cart items.
 *
 * Provides methods to:
 * - retrieve a cart item by its identifier
 * - retrieve all cart items for a specific user
 * - add a new item or update an existing one
 * - update the quantity of an existing cart item
 * - delete individual cart items
 * - clear all items from a user's cart
 *
 * Implementations should enforce business rules such as
 * validating user ownership and stock availability.
 *
 * @since 1.0.0
 */
public interface CartItemService {

    /**
     * Retrieves a single cart item by its identifier.
     *
     * @param cartItemId the identifier of the cart item to retrieve
     * @return the {@link CartItemResponseDto} for the given cart item
     */
    CartItemResponseDto getCartItemById(Long cartItemId);

    /**
     * Retrieves all cart items associated with a given user.
     *
     * @param userId the identifier of the user whose cart items to fetch
     * @return a list of {@link CartItemResponseDto} for that user
     */
    List<CartItemResponseDto> getCartItemsByUserId(Long userId);

    /**
     * Adds a new item to the cart or updates an existing one if it already exists.
     *
     * @param cartItemRequestDto the data for the cart item to add or update
     * @return the created or updated {@link CartItemResponseDto}
     */
    CartItemResponseDto addOrUpdateCartItem(CartItemRequestDto cartItemRequestDto);

    /**
     * Updates only the quantity of an existing cart item.
     *
     * @param cartItemId the identifier of the cart item to update
     * @param updateDto  the DTO containing the new quantity
     * @return the updated {@link CartItemResponseDto}
     */
    CartItemResponseDto updateCartItemQuantity(Long cartItemId, UpdateCartItemQuantityDto updateDto);

    /**
     * Deletes a single cart item by its identifier.
     *
     * @param cartItemId the identifier of the cart item to delete
     */
    void deleteCartItem(Long cartItemId);

    /**
     * Removes all items from the cart of a specific user.
     *
     * @param userId the identifier of the user whose cart is to be cleared
     */
    void clearUserCart(Long userId);
}
