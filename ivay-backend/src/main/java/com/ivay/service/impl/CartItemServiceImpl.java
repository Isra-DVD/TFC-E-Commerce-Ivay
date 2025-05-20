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

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    private static final String CART_ITEM_NOT_FOUND = "CartItem with id %d not found";
    private static final String USER_NOT_FOUND = "User with id %d not found (for cart operation)";
    private static final String PRODUCT_NOT_FOUND = "Product with id %d not found (for cart operation)";

    private CartItem validateAndGetCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CART_ITEM_NOT_FOUND, cartItemId)));
    }

    private UserEntity validateAndGetUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND, userId)));
    }

    private Product validateAndGetProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, productId)));
    }
    
    public boolean isOwner(Long cartItemId, String username) {
        CartItem cartItem = validateAndGetCartItem(cartItemId);
        return cartItem.getUser().getName().equals(username);
    }

    @Override
    public CartItemResponseDto getCartItemById(Long cartItemId) {
        log.info("Fetching cart item with id: {}", cartItemId);
        CartItem cartItem = validateAndGetCartItem(cartItemId);
        return cartItemMapper.toCartItemResponse(cartItem);
    }

    @Override
    public List<CartItemResponseDto> getCartItemsByUserId(Long userId) {
        log.info("Fetching cart items for user id: {}", userId);
        validateAndGetUser(userId);
        List<CartItem> cartItems = cartItemRepository.findByUser_Id(userId);
        return cartItems.stream()
                .map(cartItemMapper::toCartItemResponse)
                .toList();
    }

    @Override
    public CartItemResponseDto addOrUpdateCartItem(CartItemRequestDto cartItemRequestDto) {
        Long userId = cartItemRequestDto.getUserId();
        Long productId = cartItemRequestDto.getProductId();
        Integer quantity = cartItemRequestDto.getQuantity();

        log.info("Attempting to add/update cart item for user {} and product {}", userId, productId);

        UserEntity user = validateAndGetUser(userId);
        Product product = validateAndGetProduct(productId);

        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByUser_IdAndProduct_Id(userId, productId);

        CartItem cartItemToSave;
        if (existingCartItemOpt.isPresent()) {
            log.info("Product {} already in cart for user {}. Updating quantity.", productId, userId);
            cartItemToSave = existingCartItemOpt.get();
            cartItemToSave.setQuantity(cartItemToSave.getQuantity() + quantity); 
        } else {
            log.info("Product {} not in cart for user {}. Creating new cart item.", productId, userId);
            cartItemToSave = new CartItem();
            cartItemToSave.setUser(user);
            cartItemToSave.setProduct(product);
            cartItemToSave.setQuantity(quantity);
        }

        if (product.getStock() != null && cartItemToSave.getQuantity() > product.getStock()) {
            log.warn("Insufficient stock for product {}. Requested: {}, Available: {}", productId, cartItemToSave.getQuantity(), product.getStock());
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }

        CartItem savedCartItem = cartItemRepository.save(cartItemToSave);
        log.info("Saved cart item with id: {}", savedCartItem.getId());
        return cartItemMapper.toCartItemResponse(savedCartItem);
    }

    @Override
    public CartItemResponseDto updateCartItemQuantity(Long cartItemId, UpdateCartItemQuantityDto updateDto) {
        log.info("Updating quantity for cart item id: {}", cartItemId);
        CartItem cartItem = validateAndGetCartItem(cartItemId);

        Product product = cartItem.getProduct();
        if (product.getStock() != null && updateDto.getQuantity() > product.getStock()) {
             log.warn("Insufficient stock for product {}. Requested: {}, Available: {}", product.getId(), updateDto.getQuantity(), product.getStock());
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }

        cartItem.setQuantity(updateDto.getQuantity());
        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        log.info("Updated quantity for cart item id: {}", updatedCartItem.getId());
        return cartItemMapper.toCartItemResponse(updatedCartItem);
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        log.info("Attempting to delete cart item with id: {}", cartItemId);
        CartItem cartItem = validateAndGetCartItem(cartItemId); 
        cartItemRepository.delete(cartItem);
        log.info("Deleted cart item with id: {}", cartItemId);
    }

    @Override
    public void clearUserCart(Long userId) {
        log.info("Attempting to clear cart for user id: {}", userId);
        validateAndGetUser(userId); 
        cartItemRepository.deleteByUser_Id(userId); 
        log.info("Cleared cart for user id: {}", userId);
    }
}
