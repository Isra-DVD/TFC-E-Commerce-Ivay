package com.ivay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.dtos.categorydto.CategoryRequestDto;
import com.ivay.dtos.categorydto.CategoryResponseDto;
import com.ivay.dtos.productdto.ProductResponseDto;
import com.ivay.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class CategoryController {

    private final CategoryService categoryService;

    // GET /api/categories
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        ApiResponseDto<List<CategoryResponseDto>> response = new ApiResponseDto<>("Categories fetched successfully", HttpStatus.OK.value(), categories);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/categories/filter?name=...
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<CategoryResponseDto>>> findCategoriesByName(@RequestParam String name) {
        List<CategoryResponseDto> categories = categoryService.findCategoriesByName(name);
        ApiResponseDto<List<CategoryResponseDto>> response = new ApiResponseDto<>("Categories filtered by name successfully", HttpStatus.OK.value(), categories);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/categories/{categoryId}
    @GetMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> getCategoryById(@PathVariable Long categoryId) {
        CategoryResponseDto category = categoryService.getCategoryById(categoryId);
        ApiResponseDto<CategoryResponseDto> response = new ApiResponseDto<>("Category fetched successfully", HttpStatus.OK.value(), category);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

     // GET /api/categories/{categoryId}/products
    @GetMapping(value = "/{categoryId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<ProductResponseDto> products = categoryService.getProductsByCategoryId(categoryId);
        ApiResponseDto<List<ProductResponseDto>> response = new ApiResponseDto<>("Products for category fetched successfully", HttpStatus.OK.value(), products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /api/categories
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto createdCategory = categoryService.createCategory(categoryRequestDto);
        ApiResponseDto<CategoryResponseDto> response = new ApiResponseDto<>("Category created successfully", HttpStatus.CREATED.value(), createdCategory);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/categories/{categoryId}
    @PutMapping(value = "/{categoryId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> updateCategory(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryId, categoryRequestDto);
        ApiResponseDto<CategoryResponseDto> response = new ApiResponseDto<>("Category updated successfully", HttpStatus.OK.value(), updatedCategory);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE /api/categories/{categoryId}
    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}