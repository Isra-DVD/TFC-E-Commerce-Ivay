package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.categorydto.CategoryRequestDto;
import com.ivay.dtos.categorydto.CategoryResponseDto;
import com.ivay.entity.Category;

/**
 * Mapper interface for converting between Category entities and their DTOs.
 *
 * Provides methods to
 * - create a {@link Category} entity from a {@link CategoryRequestDto}
 * - create a {@link CategoryResponseDto} from a {@link Category} entity
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    /**
     * Maps a {@link CategoryRequestDto} to a {@link Category} entity.
     *
     * Ignores the id and products fields on the entity, since the id is auto-generated
     * and products are managed separately.
     *
     * @param categoryRequestDto the DTO containing category data from the client
     * @return a new Category entity populated with data from the DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toCategory(CategoryRequestDto categoryRequestDto);

    /**
     * Maps a {@link Category} entity to a {@link CategoryResponseDto}.
     *
     * Copies all matching fields from the entity into the response DTO.
     *
     * @param category the entity retrieved from the database
     * @return a CategoryResponseDto containing data for API responses
     */
    CategoryResponseDto toCategoryResponse(Category category);
}
