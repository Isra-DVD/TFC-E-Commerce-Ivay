package com.ivay.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.ivay.dtos.categorydto.CategoryRequestDto;
import com.ivay.dtos.categorydto.CategoryResponseDto;
import com.ivay.dtos.productdto.ProductResponseDto;
import com.ivay.entity.Category;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.CategoryMapper;
import com.ivay.mappers.ProductMapper;
import com.ivay.repository.CategoryRepository;
import com.ivay.service.CategoryService;

import java.util.List;

/**
 * Service implementation for managing product categories.
 *
 * Provides methods to retrieve, create, update, and delete categories,
 * as well as to fetch products belonging to a given category.
 *
 * All operations are logged and executed within a transactional context.
 *
 * @since 1.0.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    private static final String CATEGORY_NOT_FOUND = "Category with id %d not found";

    /**
     * Fetches a {@link Category} entity by its identifier or throws.
     *
     * @param categoryId the id of the category to retrieve
     * @return the found {@code Category}
     * @throws ResourceNotFoundException if no category exists with the given id
     */
    private Category validateAndGetCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(CATEGORY_NOT_FOUND, categoryId)));
    }

    /**
     * Retrieves all categories in the system.
     *
     * @return a list of {@link CategoryResponseDto} for all categories
     */
    @Override
    public List<CategoryResponseDto> getAllCategories() {
        log.info("Fetching all categories");
        return categoryRepository.findAll().stream()
            .map(categoryMapper::toCategoryResponse)
            .toList();
    }

    /**
     * Finds categories whose name contains the given substring, case-insensitive.
     *
     * @param name substring to search for in category names
     * @return a list of matching {@link CategoryResponseDto}
     */
    @Override
    public List<CategoryResponseDto> findCategoriesByName(String name) {
        log.info("Finding categories with name containing: {}", name);
        return categoryRepository.findByNameContainingIgnoreCase(name).stream()
            .map(categoryMapper::toCategoryResponse)
            .toList();
    }

    /**
     * Retrieves a single category by its identifier.
     *
     * @param categoryId the id of the category to retrieve
     * @return the {@link CategoryResponseDto} for the found category
     * @throws ResourceNotFoundException if no category exists with the given id
     */
    @Override
    public CategoryResponseDto getCategoryById(Long categoryId) {
        log.info("Fetching category with id: {}", categoryId);
        Category category = validateAndGetCategory(categoryId);
        return categoryMapper.toCategoryResponse(category);
    }

    /**
     * Creates a new category based on the provided data.
     *
     * @param categoryRequestDto the data for the new category
     * @return the created {@link CategoryResponseDto}
     */
    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        log.info("Creating new category with name: {}", categoryRequestDto.getName());
        Category category = categoryMapper.toCategory(categoryRequestDto);
        Category saved = categoryRepository.save(category);
        log.info("Created category with id: {}", saved.getId());
        return categoryMapper.toCategoryResponse(saved);
    }

    /**
     * Updates the name of an existing category.
     *
     * @param categoryId         the id of the category to update
     * @param categoryRequestDto the new data for the category
     * @return the updated {@link CategoryResponseDto}
     * @throws ResourceNotFoundException if no category exists with the given id
     */
    @Override
    public CategoryResponseDto updateCategory(
            Long categoryId,
            CategoryRequestDto categoryRequestDto) {

        log.info("Updating category with id: {}", categoryId);
        Category category = validateAndGetCategory(categoryId);
        category.setName(categoryRequestDto.getName());
        Category updated = categoryRepository.save(category);
        log.info("Updated category with id: {}", updated.getId());
        return categoryMapper.toCategoryResponse(updated);
    }

    /**
     * Deletes a category if it has no associated products.
     *
     * @param categoryId the id of the category to delete
     * @throws ResourceNotFoundException if no category exists with the given id
     * @throws IllegalStateException     if the category has associated products
     */
    @Override
    public void deleteCategory(Long categoryId) {
        log.info("Attempting to delete category with id: {}", categoryId);
        Category category = validateAndGetCategory(categoryId);

        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            log.warn("Cannot delete category {}: associated products exist", categoryId);
            throw new IllegalStateException(
                "Cannot delete category with associated products. "
                + "Please reassign or delete products first.");
        }

        categoryRepository.delete(category);
        log.info("Deleted category with id: {}", categoryId);
    }

    /**
     * Retrieves all products belonging to a specific category.
     *
     * @param categoryId the id of the category
     * @return a list of {@link ProductResponseDto} for the category's products
     * @throws ResourceNotFoundException if no category exists with the given id
     */
    @Override
    public List<ProductResponseDto> getProductsByCategoryId(Long categoryId) {
        log.info("Fetching products for category id: {}", categoryId);
        Category category = validateAndGetCategory(categoryId);
        return category.getProducts().stream()
            .map(productMapper::toProductResponse)
            .toList();
    }
}
