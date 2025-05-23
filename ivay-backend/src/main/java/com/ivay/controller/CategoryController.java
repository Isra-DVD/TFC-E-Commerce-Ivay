package com.ivay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.dtos.categorydto.CategoryRequestDto;
import com.ivay.dtos.categorydto.CategoryResponseDto;
import com.ivay.dtos.productdto.ProductResponseDto;
import com.ivay.service.CategoryService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;

/**
 * REST controller for managing product categories.
 *
 * Exposes endpoints to list, filter, retrieve, create, update, and delete
 * categories, as well as to fetch products by category.
 *
 * All responses are wrapped in an {@link ApiResponseDto} or return an error
 * payload {@link ApiError}.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "http://localhost:3001",
	    "http://localhost:5173",
	    "http://localhost:5174",
	    "http://localhost:5678"
	})@Tag(name = "Category", description = "Endpoints for managing categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Retrieve all categories.
     *
     * @return HTTP 200 with list of {@link CategoryResponseDto}
     */
    @Operation(
        summary     = "Fetch all categories",
        description = "Retrieve a list of all categories in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categories fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { CategoryResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:00:00.000Z",
                      "message": "Categories fetched successfully",
                      "code": 200,
                      "data": [
                        { "id": 1, "name": "Electronics" },
                        { "id": 2, "name": "Books" }
                      ]
                    }
                    """
                )
            )
        )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<CategoryResponseDto>>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        ApiResponseDto<List<CategoryResponseDto>> response =
            new ApiResponseDto<>("Categories fetched successfully", HttpStatus.OK.value(), categories);
        return ResponseEntity.ok(response);
    }

    /**
     * Filter categories by name containing the provided query.
     *
     * @param name substring to filter categories by
     * @return HTTP 200 with list of matching categories
     */
    @Operation(
        summary     = "Filter categories by name",
        description = "Retrieve categories whose names contain the given query parameter"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categories filtered by name successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { CategoryResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:02:00.000Z",
                      "message": "Categories filtered by name successfully",
                      "code": 200,
                      "data": [
                        { "id": 2, "name": "Books" }
                      ]
                    }
                    """
                )
            )
        )
    })
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<CategoryResponseDto>>> findCategoriesByName(
        @Parameter(description = "Name substring to filter by", required = true)
        @RequestParam String name
    ) {
        List<CategoryResponseDto> categories = categoryService.findCategoriesByName(name);
        ApiResponseDto<List<CategoryResponseDto>> response =
            new ApiResponseDto<>("Categories filtered by name successfully", HttpStatus.OK.value(), categories);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a single category by its ID.
     *
     * @param categoryId identifier of the category
     * @return HTTP 200 with the category details
     */
    @Operation(
        summary     = "Fetch a category by ID",
        description = "Retrieve a single category using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { CategoryResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:04:00.000Z",
                      "message": "Category fetched successfully",
                      "code": 200,
                      "data": { "id": 1, "name": "Electronics" }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/{categoryId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> getCategoryById(
        @Parameter(description = "Identifier of the category", required = true)
        @PathVariable Long categoryId
    ) {
        CategoryResponseDto category = categoryService.getCategoryById(categoryId);
        ApiResponseDto<CategoryResponseDto> response =
            new ApiResponseDto<>("Category fetched successfully", HttpStatus.OK.value(), category);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all products belonging to a specific category.
     *
     * @param categoryId identifier of the category
     * @return HTTP 200 with list of products
     */
    @Operation(
        summary     = "Fetch products by category ID",
        description = "Retrieve all products belonging to a specific category"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Products for category fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { ProductResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:06:00.000Z",
                      "message": "Products for category fetched successfully",
                      "code": 200,
                      "data": [
                        { "id": 5, "name": "Smartphone", "price": 699.99 },
                        { "id": 6, "name": "Laptop", "price": 1299.00 }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/{categoryId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getProductsByCategoryId(
        @Parameter(description = "Identifier of the category", required = true)
        @PathVariable Long categoryId
    ) {
        List<ProductResponseDto> products = categoryService.getProductsByCategoryId(categoryId);
        ApiResponseDto<List<ProductResponseDto>> response =
            new ApiResponseDto<>("Products for category fetched successfully", HttpStatus.OK.value(), products);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new category.
     *
     * @param categoryRequestDto payload containing the category name
     * @return HTTP 201 with the created category
     */
    @Operation(
        summary     = "Create a new category",
        description = "Add a new category to the database"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Category created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { CategoryResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:08:00.000Z",
                      "message": "Category created successfully",
                      "code": 201,
                      "data": { "id": 3, "name": "Toys" }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> createCategory(
        @Parameter(description = "Category object to create", required = true,
                   schema = @Schema(implementation = CategoryRequestDto.class))
        @Valid @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        CategoryResponseDto created = categoryService.createCategory(categoryRequestDto);
        ApiResponseDto<CategoryResponseDto> response =
            new ApiResponseDto<>("Category created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update the name of an existing category.
     *
     * @param categoryId        identifier of the category to update
     * @param categoryRequestDto new category data
     * @return HTTP 200 with the updated category
     */
    @Operation(
        summary     = "Update an existing category",
        description = "Modify a category's name using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Category updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { CategoryResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T18:10:00.000Z",
                      "message": "Category updated successfully",
                      "code": 200,
                      "data": { "id": 1, "name": "Electronics & Gadgets" }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PutMapping(
        value    = "/{categoryId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponseDto<CategoryResponseDto>> updateCategory(
        @Parameter(description = "Identifier of the category", required = true)
        @PathVariable Long categoryId,
        @Parameter(description = "Updated category object", required = true,
                   schema = @Schema(implementation = CategoryRequestDto.class))
        @Valid @RequestBody CategoryRequestDto categoryRequestDto
    ) {
        CategoryResponseDto updated = categoryService.updateCategory(categoryId, categoryRequestDto);
        ApiResponseDto<CategoryResponseDto> response =
            new ApiResponseDto<>("Category updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a category by its ID.
     *
     * @param categoryId identifier of the category to delete
     * @return HTTP 204 No Content
     */
    @Operation(
        summary     = "Delete a category by ID",
        description = "Remove a category from the database using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Category deleted successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
        @Parameter(description = "Identifier of the category", required = true)
        @PathVariable Long categoryId
    ) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
