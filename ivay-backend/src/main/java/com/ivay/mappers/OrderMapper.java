package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.orderdto.OrderRequestDto;
import com.ivay.dtos.orderdto.OrderResponseDto;
import com.ivay.entity.Order;

/**
 * Mapper interface for converting between {@link Order} entities and their DTOs.
 *
 * Provides methods to:
 * - create an {@link Order} entity from an {@link OrderRequestDto}
 * - create an {@link OrderResponseDto} from an {@link Order} entity
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Maps an {@link OrderRequestDto} to an {@link Order} entity.
     *
     * Ignores the id, user, and orderItems fields on the entity, since
     * the id is auto-generated and associations are set elsewhere.
     *
     * @param orderRequestDto the DTO containing order data from the client
     * @return a new Order entity populated with data from the DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    Order toOrder(OrderRequestDto orderRequestDto);

    /**
     * Maps an {@link Order} entity to an {@link OrderResponseDto}.
     *
     * Extracts the user identifier into the dto and copies other fields.
     *
     * @param order the entity retrieved from the database
     * @return an OrderResponseDto containing data for API responses
     */
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toOrderResponse(Order order);
}
