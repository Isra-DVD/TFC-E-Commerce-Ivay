package com.ivay.service;

import java.util.List;

import com.ivay.dtos.categorydto.CategoryRequestDto;
import com.ivay.dtos.categorydto.CategoryResponseDto;
import com.ivay.dtos.productdto.ProductResponseDto;

/**
 * Service interface for managing product categories.
 *
 * Provides methods to:
 * - retrieve all categories or by name
 * - retrieve, create, update, and delete a single category
 * - fetch products belonging to a specific category
 *
 * @since 1.0.0
 */
public interface CategoryService {

    /**
     * Retrieves all categories in the system.
     *
     * @return a list of {@link CategoryResponseDto} representing all categories
     */
    List<CategoryResponseDto> getAllCategories();

    /**
     * Finds categories whose name contains the given substring, case-insensitive.
     *
     * @param name substring to search for within category names
     * @return a list of matching {@link CategoryResponseDto}
     */
    List<CategoryResponseDto> findCategoriesByName(String name);

    /**
     * Retrieves a single category by its identifier.
     *
     * @param categoryId the identifier of the category to retrieve
     * @return the {@link CategoryResponseDto} for the given ID
     */
    CategoryResponseDto getCategoryById(Long categoryId);

    /**
     * Creates a new category.
     *
     * @param categoryRequestDto the data for the new category
     * @return the created {@link CategoryResponseDto}
     */
    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    /**
     * Updates an existing category.
     *
     * @param categoryId the identifier of the category to update
     * @param categoryRequestDto the new data for the category
     * @return the updated {@link CategoryResponseDto}
     */
    CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto);

    /**
     * Deletes a category by its identifier.
     *
     * @param categoryId the identifier of the category to delete
     */
    void deleteCategory(Long categoryId);

    /**
     * Retrieves all products associated with a specific category.
     *
     * @param categoryId the identifier of the category
     * @return a list of {@link ProductResponseDto} for products in that category
     */
    List<ProductResponseDto> getProductsByCategoryId(Long categoryId);
}
