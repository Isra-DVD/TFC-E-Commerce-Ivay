package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.orderdto.OrderRequestDto;
import com.ivay.dtos.orderdto.OrderResponseDto;
import com.ivay.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) 
    @Mapping(target = "orderItems", ignore = true) 
    Order toOrder(OrderRequestDto orderRequestDto);

    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toOrderResponse(Order order);
}
