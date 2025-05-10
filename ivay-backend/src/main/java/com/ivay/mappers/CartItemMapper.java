package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.cartitemdto.CartItemRequestDto;
import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.entity.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) 
    @Mapping(target = "product", ignore = true) 
    CartItem toCartItem(CartItemRequestDto cartItemRequestDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    CartItemResponseDto toCartItemResponse(CartItem cartItem);
}
