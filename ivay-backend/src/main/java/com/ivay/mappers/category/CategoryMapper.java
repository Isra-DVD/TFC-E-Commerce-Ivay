package com.ivay.mappers.category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.categorydto.CategoryRequestDto;
import com.ivay.dtos.categorydto.CategoryResponseDto;
import com.ivay.entity.Category;


@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true) 
    Category toCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto toCategoryResponse(Category category);
}
