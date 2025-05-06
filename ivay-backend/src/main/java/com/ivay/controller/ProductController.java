package com.ivay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
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
 * Controller for managing products.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
@Tag(name = "Product", description = "Endpoints for managing products")
public class ProductController {

	private final ProductService productService;

	@Operation(
			summary     = "Fetch all products",
			description = "Retrieve a list of all products from the database",
			tags        = { "Product" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Products fetched successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { ProductResponseDto.class }
								),
						examples = @ExampleObject(
								name  = "ProductsList",
								value = """
										{
										  "timestamp": "2025-05-06T16:30:00.000Z",
										  "message": "Products fetched successfully",
										  "code": 200,
										  "data": [
										    { "productId": 1, "name": "Laptop", "description": "Powerful laptop", "price": 1200.00 },
										    { "productId": 2, "name": "Phone",  "description": "Smartphone",       "price": 800.00 }
										  ]
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "500",
				description  = "Internal server error",
				content      = @Content(
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
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary     = "Filter products by name",
			description = "Retrieve products whose names contain the given query string",
			tags        = { "Product" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Products filtered successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { ProductResponseDto.class }
								),
						examples = @ExampleObject(
								name  = "FilteredProducts",
								value = """
										{
										  "timestamp": "2025-05-06T16:31:00.000Z",
										  "message": "Products filtered by name successfully",
										  "code": 200,
										  "data": [
										    { "productId": 2, "name": "Phone", "description": "Smartphone", "price": 800.00 }
										  ]
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "500",
				description  = "Internal server error",
				content      = @Content(
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
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary     = "Fetch a product by ID",
			description = "Retrieve a single product using its ID",
			tags        = { "Product" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Product fetched successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { ProductResponseDto.class }
								),
						examples = @ExampleObject(
								name  = "ProductDetail",
								value = """
										{
										  "timestamp": "2025-05-06T16:32:00.000Z",
										  "message": "Product fetched successfully",
										  "code": 200,
										  "data": { "productId": 1, "name": "Laptop", "description": "Powerful laptop", "price": 1200.00 }
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "404",
				description  = "Product not found",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(implementation = ApiError.class),
						examples = @ExampleObject(
								name  = "NotFound",
								value = """
										{
										  "timestamp": "2025-05-06T16:32:30.000Z",
										  "status": 404,
										  "error": "Resource Not Found",
										  "message": "Product with id 99 not found"
										}
										"""
								)
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
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary     = "Fetch order items by product ID",
			description = "Retrieve all order items that include the given product",
			tags        = { "Product" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Order items fetched successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { OrderItemResponseDto.class }
								),
						examples = @ExampleObject(
								name  = "OrderItemsList",
								value = """
										{
										  "timestamp": "2025-05-06T16:33:00.000Z",
										  "message": "Order items for product fetched successfully",
										  "code": 200,
										  "data": [
										    { "orderItemId": 10, "orderId": 5, "productId": 1, "quantity": 2, "unitPrice": 1200.00 }
										  ]
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "404",
				description  = "Product not found",
				content      = @Content(
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
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary     = "Fetch cart items by product ID",
			description = "Retrieve all cart items that include the given product",
			tags        = { "Product" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Cart items fetched successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { CartItemResponseDto.class }
								),
						examples = @ExampleObject(
								name  = "CartItemsList",
								value = """
										{
										  "timestamp": "2025-05-06T16:34:00.000Z",
										  "message": "Cart items for product fetched successfully",
										  "code": 200,
										  "data": [
										    { "cartItemId": 20, "userId": 3, "productId": 1, "quantity": 1 }
										  ]
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "404",
				description  = "Product not found",
				content      = @Content(
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
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary     = "Create a new product",
			description = "Add a new product to the database",
			tags        = { "Product" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "201",
				description  = "Product created successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { ProductResponseDto.class }
								),
						examples = @ExampleObject(
								name  = "ProductCreated",
								value = """
										{
										  "timestamp": "2025-05-06T16:35:00.000Z",
										  "message": "Product created successfully",
										  "code": 201,
										  "data": { "productId": 3, "name": "Tablet", "description": "Android tablet", "price": 500.00 }
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "400",
				description  = "Invalid input",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(implementation = ApiError.class)
						)
				)
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(
			@Parameter(description = "Product to create", required = true, schema = @Schema(implementation = ProductRequestDto.class))
			@Valid @RequestBody ProductRequestDto productRequestDto
			) {
		ProductResponseDto createdProduct = productService.createProduct(productRequestDto);
		ApiResponseDto<ProductResponseDto> response =
				new ApiResponseDto<>("Product created successfully", HttpStatus.CREATED.value(), createdProduct);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(
			summary     = "Update an existing product",
			description = "Update the details of an existing product using its ID",
			tags        = { "Product" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Product updated successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { ProductResponseDto.class }
								),
						examples = @ExampleObject(
								name  = "ProductUpdated",
								value = """
										{
										  "timestamp": "2025-05-06T16:36:00.000Z",
										  "message": "Product updated successfully",
										  "code": 200,
										  "data": { "productId": 1, "name": "Laptop Pro", "description": "Upgraded laptop", "price": 1500.00 }
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "400",
				description  = "Invalid input",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(implementation = ApiError.class)
						)
				),
		@ApiResponse(
				responseCode = "404",
				description  = "Product not found",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(implementation = ApiError.class)
						)
				)
	})
	@PutMapping(value = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProduct(
			@Parameter(description = "Identifier of the product", required = true)
			@PathVariable Long productId,
			@Parameter(description = "Updated product object", required = true, schema = @Schema(implementation = ProductRequestDto.class))
			@Valid @RequestBody ProductRequestDto productRequestDto
			) {
		ProductResponseDto updatedProduct = productService.updateProduct(productId, productRequestDto);
		ApiResponseDto<ProductResponseDto> response =
				new ApiResponseDto<>("Product updated successfully", HttpStatus.OK.value(), updatedProduct);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary     = "Delete a product by ID",
			description = "Delete a product from the database using its ID",
			tags        = { "Product" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "204",
				description  = "Product deleted successfully",
				content      = @Content
				),
		@ApiResponse(
				responseCode = "404",
				description  = "Product not found",
				content      = @Content(
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
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
