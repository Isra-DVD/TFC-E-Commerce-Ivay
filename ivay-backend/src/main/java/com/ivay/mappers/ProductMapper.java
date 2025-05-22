package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.productdto.ProductRequestDto;
import com.ivay.dtos.productdto.ProductResponseDto;
import com.ivay.entity.Product;

/**
 * Mapper interface for converting between Product entities and their DTOs.
 *
 * Provides methods to
 * - create a {@link Product} entity from a {@link ProductRequestDto}
 * - create a {@link ProductResponseDto} from a {@link Product} entity
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Maps a {@link ProductRequestDto} to a {@link Product} entity.
     *
     * Ignores the id, category, supplier, orderItems and cartItems fields on the
     * entity, since the id is auto-generated and associations are managed separately.
     *
     * @param productRequestDto the DTO containing product data from the client
     * @return a new Product entity populated with data from the DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Product toProduct(ProductRequestDto productRequestDto);

    /**
     * Maps a {@link Product} entity to a {@link ProductResponseDto}.
     *
     * Extracts the category and supplier identifiers into the DTO
     * and copies all other matching fields.
     *
     * @param product the entity retrieved from the database
     * @return a ProductResponseDto containing data for API responses
     */
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "supplier.id", target = "supplierId")
    ProductResponseDto toProductResponse(Product product);
}
