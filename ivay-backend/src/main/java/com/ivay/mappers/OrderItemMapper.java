package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.orderitemdto.OrderItemRequestDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.entity.OrderItem;

/**
 * Mapper interface for converting between {@link OrderItem} entities and their DTOs.
 *
 * Provides methods to:
 * - create an {@link OrderItem} entity from an {@link OrderItemRequestDto}
 * - create an {@link OrderItemResponseDto} from an {@link OrderItem} entity
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    /**
     * Maps an {@link OrderItemRequestDto} to an {@link OrderItem} entity.
     *
     * Ignores the id, order, and product fields on the entity, since
     * the id is auto-generated and associations are set elsewhere.
     *
     * @param orderItemRequestDto the DTO containing order item data
     * @return a new OrderItem entity populated with data from the DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderItem toOrderItem(OrderItemRequestDto orderItemRequestDto);

    /**
     * Maps an {@link OrderItem} entity to an {@link OrderItemResponseDto}.
     *
     * Extracts the order and product identifiers into the DTO.
     *
     * @param orderItem the entity retrieved from the database
     * @return an OrderItemResponseDto containing data for API responses
     */
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    OrderItemResponseDto toOrderItemResponse(OrderItem orderItem);
}
