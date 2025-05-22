package com.ivay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.dtos.productdto.PaginatedProductResponseDto;
import com.ivay.dtos.productdto.ProductRequestDto;
import com.ivay.dtos.productdto.ProductResponseDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.service.ProductService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;

/**
 * REST controller for managing products.
 *
 * Provides endpoints to list, filter, paginate, retrieve,
 * create, update, and delete products, as well as to fetch
 * related order items and cart items.
 *
 * All responses are wrapped in {@link ApiResponseDto} or
 * return an {@link ApiError} payload on error.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
@Tag(name = "Product", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    /**
     * Retrieve all products.
     *
     * @return HTTP 200 with list of {@link ProductResponseDto}
     */
    @Operation(
        summary     = "Fetch all products",
        description = "Retrieve a list of all products"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Products fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { ProductResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:30:00.000Z",
                      "message": "Products fetched successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 1,
                          "name": "Laptop",
                          "description": "Powerful laptop",
                          "price": 1200.00,
                          "stock": 50,
                          "discount": 0.10,
                          "imageUrl": "http://example.com/laptop.jpg",
                          "categoryId": 1,
                          "supplierId": 2
                        },
                        {
                          "id": 2,
                          "name": "Phone",
                          "description": "Smartphone",
                          "price": 800.00,
                          "stock": 150,
                          "discount": 0.05,
                          "imageUrl": "http://example.com/phone.jpg",
                          "categoryId": 1,
                          "supplierId": 3
                        }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        ApiResponseDto<List<ProductResponseDto>> response =
            new ApiResponseDto<>("Products fetched successfully", HttpStatus.OK.value(), products);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve products with pagination.
     *
     * @param page zero-based page index
     * @param size number of items per page
     * @return HTTP 200 with {@link PaginatedProductResponseDto}
     */
    @Operation(
        summary     = "Fetch products paginated",
        description = "Retrieve a paginated list of products, sorted by ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Products fetched successfully (paginated)",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { PaginatedProductResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:45:00.000Z",
                      "message": "Products fetched successfully (paginated)",
                      "code": 200,
                      "data": {
                        "content": [
                          {
                            "id": 1,
                            "name": "Laptop",
                            "description": "Powerful laptop",
                            "price": 1200.00,
                            "stock": 50,
                            "discount": 0.10,
                            "imageUrl": "http://example.com/laptop.jpg",
                            "categoryId": 1,
                            "supplierId": 2
                          }
                        ],
                        "page": 0,
                        "size": 1,
                        "totalElements": 100,
                        "totalPages": 100,
                        "hasNext": true
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Page not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<PaginatedProductResponseDto>> getProductsPaginated(
        @Parameter(description = "Page index (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Number of items per page", example = "20") @RequestParam(defaultValue = "20") int size
    ) {
        PaginatedProductResponseDto paginated = productService.getProductsPaginated(page, size);
        ApiResponseDto<PaginatedProductResponseDto> response =
            new ApiResponseDto<>("Products fetched successfully (paginated)", HttpStatus.OK.value(), paginated);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve products whose names contain the given substring.
     *
     * @param name substring to filter by
     * @return HTTP 200 with list of {@link ProductResponseDto}
     */
    @Operation(
        summary     = "Filter products by name",
        description = "Retrieve products whose names contain the given query string"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Products filtered successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { ProductResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:31:00.000Z",
                      "message": "Products filtered by name successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 2,
                          "name": "Phone",
                          "description": "Smartphone",
                          "price": 800.00,
                          "stock": 150,
                          "discount": 0.05,
                          "imageUrl": "http://example.com/phone.jpg",
                          "categoryId": 1,
                          "supplierId": 3
                        }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> findProductsByName(
        @Parameter(description = "Substring to search in product names", required = true)
        @RequestParam String name
    ) {
        List<ProductResponseDto> products = productService.findProductsByName(name);
        ApiResponseDto<List<ProductResponseDto>> response =
            new ApiResponseDto<>("Products filtered by name successfully", HttpStatus.OK.value(), products);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a single product by its ID.
     *
     * @param productId identifier of the product
     * @return HTTP 200 with {@link ProductResponseDto}
     */
    @Operation(
        summary     = "Fetch a product by ID",
        description = "Retrieve a single product using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { ProductResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:32:00.000Z",
                      "message": "Product fetched successfully",
                      "code": 200,
                      "data": {
                        "id": 1,
                        "name": "Laptop",
                        "description": "Powerful laptop",
                        "price": 1200.00,
                        "stock": 50,
                        "discount": 0.10,
                        "imageUrl": "http://example.com/laptop.jpg",
                        "categoryId": 1,
                        "supplierId": 2
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> getProductById(
        @Parameter(description = "Identifier of the product", required = true)
        @PathVariable Long productId
    ) {
        ProductResponseDto product = productService.getProductById(productId);
        ApiResponseDto<ProductResponseDto> response =
            new ApiResponseDto<>("Product fetched successfully", HttpStatus.OK.value(), product);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all order items that include the given product.
     *
     * @param productId identifier of the product
     * @return HTTP 200 with list of {@link OrderItemResponseDto}
     */
    @Operation(
        summary     = "Fetch order items by product ID",
        description = "Retrieve all order items that include the given product"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order items fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { OrderItemResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:33:00.000Z",
                      "message": "Order items for product fetched successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 10,
                          "orderId": 5,
                          "productId": 1,
                          "quantity": 2,
                          "discount": 0.00,
                          "price": 1200.00,
                          "totalPrice": 2400.00
                        }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/{productId}/order-items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderItemResponseDto>>> getOrderItemsByProductId(
        @Parameter(description = "Identifier of the product", required = true)
        @PathVariable Long productId
    ) {
        List<OrderItemResponseDto> items = productService.getOrderItemsByProductId(productId);
        ApiResponseDto<List<OrderItemResponseDto>> response =
            new ApiResponseDto<>("Order items for product fetched successfully", HttpStatus.OK.value(), items);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all cart items that include the given product.
     *
     * @param productId identifier of the product
     * @return HTTP 200 with list of {@link CartItemResponseDto}
     */
    @Operation(
        summary     = "Fetch cart items by product ID",
        description = "Retrieve all cart items that include the given product"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cart items fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { CartItemResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:34:00.000Z",
                      "message": "Cart items for product fetched successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 20,
                          "userId": 3,
                          "productId": 1,
                          "quantity": 1
                        }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping(value = "/{productId}/cart-items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<CartItemResponseDto>>> getCartItemsByProductId(
        @Parameter(description = "Identifier of the product", required = true)
        @PathVariable Long productId
    ) {
        List<CartItemResponseDto> items = productService.getCartItemsByProductId(productId);
        ApiResponseDto<List<CartItemResponseDto>> response =
            new ApiResponseDto<>("Cart items for product fetched successfully", HttpStatus.OK.value(), items);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new product.
     *
     * Only users with roles SUPERADMIN, ADMIN, or MANAGER may create products.
     *
     * @param requestDto product data
     * @return HTTP 201 with created {@link ProductResponseDto}
     */
    @Operation(
        summary     = "Create a new product",
        description = "Add a new product to the database"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Product created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { ProductResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:35:00.000Z",
                      "message": "Product created successfully",
                      "code": 201,
                      "data": {
                        "id": 3,
                        "name": "Tablet",
                        "description": "Android tablet",
                        "price": 500.00,
                        "stock": 100,
                        "discount": 0.00,
                        "imageUrl": "http://example.com/tablet.jpg",
                        "categoryId": 2,
                        "supplierId": 4
                      }
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
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(
        @Parameter(description = "Product to create", required = true,
                   schema = @Schema(implementation = ProductRequestDto.class))
        @Valid @RequestBody ProductRequestDto requestDto
    ) {
        ProductResponseDto created = productService.createProduct(requestDto);
        ApiResponseDto<ProductResponseDto> response =
            new ApiResponseDto<>("Product created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing product by ID.
     *
     * Only users with roles SUPERADMIN, ADMIN, or MANAGER may update products.
     *
     * @param productId  identifier of the product
     * @param requestDto updated product data
     * @return HTTP 200 with updated {@link ProductResponseDto}
     */
    @Operation(
        summary     = "Update an existing product",
        description = "Modify the details of a product using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Product updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { ProductResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:36:00.000Z",
                      "message": "Product updated successfully",
                      "code": 200,
                      "data": {
                        "id": 1,
                        "name": "Laptop Pro",
                        "description": "Upgraded laptop",
                        "price": 1500.00,
                        "stock": 45,
                        "discount": 0.05,
                        "imageUrl": "http://example.com/laptop-pro.jpg",
                        "categoryId": 1,
                        "supplierId": 2
                      }
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
        @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PutMapping(
        value    = "/{productId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProduct(
        @Parameter(description = "Identifier of the product", required = true)
        @PathVariable Long productId,
        @Parameter(description = "Updated product object", required = true,
                   schema = @Schema(implementation = ProductRequestDto.class))
        @Valid @RequestBody ProductRequestDto requestDto
    ) {
        ProductResponseDto updated = productService.updateProduct(productId, requestDto);
        ApiResponseDto<ProductResponseDto> response =
            new ApiResponseDto<>("Product updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a product by its ID.
     *
     * Only users with roles SUPERADMIN, ADMIN, or MANAGER may delete products.
     *
     * @param productId identifier of the product
     * @return HTTP 204 No Content
     */
    @Operation(
        summary     = "Delete a product by ID",
        description = "Remove a product from the database using its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Product deleted successfully", content = @Content),
        @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<Void> deleteProduct(
        @Parameter(description = "Identifier of the product", required = true)
        @PathVariable Long productId
    ) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
