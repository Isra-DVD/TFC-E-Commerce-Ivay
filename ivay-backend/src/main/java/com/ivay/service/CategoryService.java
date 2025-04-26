package com.ivay.service;

import java.util.List;

import com.ivay.dtos.categorydto.CategoryRequestDto;
import com.ivay.dtos.categorydto.CategoryResponseDto;
import com.ivay.dtos.productdto.ProductResponseDto;

public interface CategoryService {
    List<CategoryResponseDto> getAllCategories();
    List<CategoryResponseDto> findCategoriesByName(String name);
    CategoryResponseDto getCategoryById(Long categoryId);
    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto);
    void deleteCategory(Long categoryId);
    List<ProductResponseDto> getProductsByCategoryId(Long categoryId); 
}
