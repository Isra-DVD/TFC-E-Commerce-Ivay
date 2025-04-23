package com.ivay.mappers.orderitem;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.orderitemdto.OrderItemRequestDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderItem toOrderItem(OrderItemRequestDto orderItemRequestDto);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    OrderItemResponseDto toOrderItemResponse(OrderItem orderItem);
}
