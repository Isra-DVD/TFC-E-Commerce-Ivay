package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.cartitemdto.CartItemRequestDto;
import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.entity.CartItem;

/**
 * Mapper interface for converting between {@link CartItem} entities and their DTOs.
 *
 * Provides methods to:
 * - Create a {@link CartItem} entity from a {@link CartItemRequestDto}.
 * - Create a {@link CartItemResponseDto} from a {@link CartItem} entity.
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface CartItemMapper {

    /**
     * Maps a {@link CartItemRequestDto} to a {@link CartItem} entity.
     *
     * Ignores the {@code id}, {@code user}, and {@code product} fields on the
     * entity, as those are set by service logic and persistence.
     *
     * @param cartItemRequestDto the DTO containing cart item data from the client
     * @return a new {@code CartItem} entity populated with data from the DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    CartItem toCartItem(CartItemRequestDto cartItemRequestDto);

    /**
     * Maps a {@link CartItem} entity to a {@link CartItemResponseDto}.
     *
     * Extracts the user and product identifiers from the entity and maps them
     * to the {@code userId} and {@code productId} fields on the response DTO.
     *
     * @param cartItem the entity retrieved from the database
     * @return a {@code CartItemResponseDto} containing data for API responses
     */
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    CartItemResponseDto toCartItemResponse(CartItem cartItem);
}
