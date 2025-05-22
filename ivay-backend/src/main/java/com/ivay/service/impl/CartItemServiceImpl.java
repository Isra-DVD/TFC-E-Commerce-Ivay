package com.ivay.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ivay.dtos.cartitemdto.CartItemRequestDto;
import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.dtos.cartitemdto.UpdateCartItemQuantityDto;
import com.ivay.entity.CartItem;
import com.ivay.entity.Product;
import com.ivay.entity.UserEntity;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.CartItemMapper;
import com.ivay.repository.CartItemRepository;
import com.ivay.repository.ProductRepository;
import com.ivay.repository.UserRepository;
import com.ivay.service.CartItemService;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing shopping cart items.
 *
 * Performs create, read, update and delete operations on cart items,
 * with validation of user and product existence, stock checks,
 * and supports quantity updates or clearing a user's cart.
 *
 * All operations are executed within a transactional context.
 *
 * @since 1.0.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    private static final String CART_ITEM_NOT_FOUND    = "CartItem with id %d not found";
    private static final String USER_NOT_FOUND         = "User with id %d not found (for cart operation)";
    private static final String PRODUCT_NOT_FOUND      = "Product with id %d not found (for cart operation)";

    /**
     * Retrieves a CartItem by its id, throwing if not found.
     *
     * @param cartItemId id of the cart item
     * @return the CartItem entity
     * @throws ResourceNotFoundException if no cart item exists with that id
     */
    private CartItem validateAndGetCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CART_ITEM_NOT_FOUND, cartItemId)));
    }

    /**
     * Retrieves a UserEntity by id, throwing if not found.
     *
     * @param userId id of the user
     * @return the UserEntity
     * @throws ResourceNotFoundException if no user exists with that id
     */
    private UserEntity validateAndGetUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(USER_NOT_FOUND, userId)));
    }

    /**
     * Retrieves a Product by id, throwing if not found.
     *
     * @param productId id of the product
     * @return the Product entity
     * @throws ResourceNotFoundException if no product exists with that id
     */
    private Product validateAndGetProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(PRODUCT_NOT_FOUND, productId)));
    }

    /**
     * Checks ownership of a cart item by username.
     *
     * @param cartItemId id of the cart item
     * @param username   name of the user
     * @return true if the cart item belongs to the user
     */
    public boolean isOwner(Long cartItemId, String username) {
        CartItem cartItem = validateAndGetCartItem(cartItemId);
        return cartItem.getUser().getName().equals(username);
    }

    /**
     * Fetches a cart item by its id.
     *
     * @param cartItemId id of the cart item
     * @return the CartItemResponseDto
     * @throws ResourceNotFoundException if not found
     */
    @Override
    public CartItemResponseDto getCartItemById(Long cartItemId) {
        log.info("Fetching cart item with id: {}", cartItemId);
        CartItem cartItem = validateAndGetCartItem(cartItemId);
        return cartItemMapper.toCartItemResponse(cartItem);
    }

    /**
     * Retrieves all cart items for a given user.
     *
     * @param userId id of the user
     * @return list of CartItemResponseDto for that user
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    public List<CartItemResponseDto> getCartItemsByUserId(Long userId) {
        log.info("Fetching cart items for user id: {}", userId);
        validateAndGetUser(userId);
        return cartItemRepository.findByUser_Id(userId).stream()
                .map(cartItemMapper::toCartItemResponse)
                .toList();
    }

    /**
     * Adds a new cart item or updates quantity if already present.
     *
     * Validates user and product existence, enforces stock limits,
     * and returns the resulting cart item.
     *
     * @param cartItemRequestDto request containing userId, productId, quantity
     * @return the created or updated CartItemResponseDto
     * @throws IllegalStateException      if quantity exceeds stock
     * @throws ResourceNotFoundException  if user or product not found
     */
    @Override
    public CartItemResponseDto addOrUpdateCartItem(CartItemRequestDto cartItemRequestDto) {
        Long userId    = cartItemRequestDto.getUserId();
        Long productId = cartItemRequestDto.getProductId();
        Integer quantity = cartItemRequestDto.getQuantity();

        log.info("Attempting to add/update cart item for user {} and product {}", userId, productId);

        UserEntity user    = validateAndGetUser(userId);
        Product    product = validateAndGetProduct(productId);

        Optional<CartItem> existingOpt = cartItemRepository
                .findByUser_IdAndProduct_Id(userId, productId);

        CartItem cartItemToSave;
        if (existingOpt.isPresent()) {
            cartItemToSave = existingOpt.get();
            cartItemToSave.setQuantity(cartItemToSave.getQuantity() + quantity);
        } else {
            cartItemToSave = new CartItem();
            cartItemToSave.setUser(user);
            cartItemToSave.setProduct(product);
            cartItemToSave.setQuantity(quantity);
        }

        if (product.getStock() != null && cartItemToSave.getQuantity() > product.getStock()) {
            log.warn("Insufficient stock for product {}. Requested: {}, Available: {}",
                     productId, cartItemToSave.getQuantity(), product.getStock());
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }

        CartItem saved = cartItemRepository.save(cartItemToSave);
        log.info("Saved cart item with id: {}", saved.getId());
        return cartItemMapper.toCartItemResponse(saved);
    }

    /**
     * Updates only the quantity of an existing cart item.
     *
     * Validates stock against the new quantity.
     *
     * @param cartItemId id of the cart item
     * @param updateDto  DTO containing the new quantity
     * @return the updated CartItemResponseDto
     * @throws IllegalStateException      if quantity exceeds stock
     * @throws ResourceNotFoundException  if cart item not found
     */
    @Override
    public CartItemResponseDto updateCartItemQuantity(Long cartItemId,
                                                      UpdateCartItemQuantityDto updateDto) {
        log.info("Updating quantity for cart item id: {}", cartItemId);
        CartItem cartItem = validateAndGetCartItem(cartItemId);

        Product product = cartItem.getProduct();
        if (product.getStock() != null && updateDto.getQuantity() > product.getStock()) {
            log.warn("Insufficient stock for product {}. Requested: {}, Available: {}",
                     product.getId(), updateDto.getQuantity(), product.getStock());
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }

        cartItem.setQuantity(updateDto.getQuantity());
        CartItem updated = cartItemRepository.save(cartItem);
        log.info("Updated quantity for cart item id: {}", updated.getId());
        return cartItemMapper.toCartItemResponse(updated);
    }

    /**
     * Deletes a cart item by its id.
     *
     * @param cartItemId id of the cart item to delete
     * @throws ResourceNotFoundException if cart item not found
     */
    @Override
    public void deleteCartItem(Long cartItemId) {
        log.info("Attempting to delete cart item with id: {}", cartItemId);
        CartItem cartItem = validateAndGetCartItem(cartItemId);
        cartItemRepository.delete(cartItem);
        log.info("Deleted cart item with id: {}", cartItemId);
    }

    /**
     * Clears all cart items for a given user.
     *
     * @param userId id of the user whose cart is cleared
     * @throws ResourceNotFoundException if user not found
     */
    @Override
    public void clearUserCart(Long userId) {
        log.info("Attempting to clear cart for user id: {}", userId);
        validateAndGetUser(userId);
        cartItemRepository.deleteByUser_Id(userId);
        log.info("Cleared cart for user id: {}", userId);
    }
}
