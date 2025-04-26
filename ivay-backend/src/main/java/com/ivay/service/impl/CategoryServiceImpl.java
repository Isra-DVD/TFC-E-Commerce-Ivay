package com.ivay.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor; // Using constructor injection
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ivay.dtos.categorydto.CategoryRequestDto;
import com.ivay.dtos.categorydto.CategoryResponseDto;
import com.ivay.dtos.exception.ResourceNotFoundException;
import com.ivay.dtos.productdto.ProductResponseDto;
import com.ivay.entity.Category;
import com.ivay.mappers.category.CategoryMapper;
import com.ivay.mappers.product.ProductMapper;
import com.ivay.repository.CategoryRepository;
import com.ivay.service.CategoryService;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper; // Added

    private static final String CATEGORY_NOT_FOUND = "Category with id %d not found";

    private Category validateAndGetCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CATEGORY_NOT_FOUND, categoryId)));
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        log.info("Fetching all categories");
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public List<CategoryResponseDto> findCategoriesByName(String name) {
        log.info("Finding categories with name containing: {}", name);
        return categoryRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponseDto getCategoryById(Long categoryId) {
        log.info("Fetching category with id: {}", categoryId);
        Category category = validateAndGetCategory(categoryId);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        log.info("Creating new category with name: {}", categoryRequestDto.getName());
        Category category = categoryMapper.toCategory(categoryRequestDto);
        Category savedCategory = categoryRepository.save(category);
        log.info("Created category with id: {}", savedCategory.getId());
        return categoryMapper.toCategoryResponse(savedCategory);
    }

    @Override
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto) {
        log.info("Updating category with id: {}", categoryId);
        Category category = validateAndGetCategory(categoryId);
        // Update mutable fields
        category.setName(categoryRequestDto.getName());
        Category updatedCategory = categoryRepository.save(category);
        log.info("Updated category with id: {}", updatedCategory.getId());
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        log.info("Attempting to delete category with id: {}", categoryId);
        Category category = validateAndGetCategory(categoryId);

        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            log.warn("Deletion failed: Category {} has associated products.", categoryId);
            throw new IllegalStateException("Cannot delete category with associated products. Please reassign or delete products first.");
        }

        categoryRepository.delete(category);
        log.info("Deleted category with id: {}", categoryId);
    }

    @Override
    public List<ProductResponseDto> getProductsByCategoryId(Long categoryId) {
        log.info("Fetching products for category id: {}", categoryId);
        Category category = validateAndGetCategory(categoryId);
        return category.getProducts()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
