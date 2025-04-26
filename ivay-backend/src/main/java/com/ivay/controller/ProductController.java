package com.ivay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.dtos.productdto.ProductRequestDto;
import com.ivay.dtos.productdto.ProductResponseDto;
import com.ivay.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // GET /api/products
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        ApiResponseDto<List<ProductResponseDto>> response = new ApiResponseDto<>("Products fetched successfully", HttpStatus.OK.value(), products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/products/filter?name=...
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> findProductsByName(@RequestParam String name) {
        List<ProductResponseDto> products = productService.findProductsByName(name);
        ApiResponseDto<List<ProductResponseDto>> response = new ApiResponseDto<>("Products filtered by name successfully", HttpStatus.OK.value(), products);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // GET /api/products/{productId}
    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> getProductById(@PathVariable Long productId) {
        ProductResponseDto product = productService.getProductById(productId);
        ApiResponseDto<ProductResponseDto> response = new ApiResponseDto<>("Product fetched successfully", HttpStatus.OK.value(), product);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

     // GET /api/products/{productId}/order-items
    @GetMapping(value = "/{productId}/order-items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<OrderItemResponseDto>>> getOrderItemsByProductId(@PathVariable Long productId) {
        List<OrderItemResponseDto> items = productService.getOrderItemsByProductId(productId);
        ApiResponseDto<List<OrderItemResponseDto>> response = new ApiResponseDto<>("Order items for product fetched successfully", HttpStatus.OK.value(), items);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

     // GET /api/products/{productId}/cart-items
    @GetMapping(value = "/{productId}/cart-items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<CartItemResponseDto>>> getCartItemsByProductId(@PathVariable Long productId) {
        List<CartItemResponseDto> items = productService.getCartItemsByProductId(productId);
        ApiResponseDto<List<CartItemResponseDto>> response = new ApiResponseDto<>("Cart items for product fetched successfully", HttpStatus.OK.value(), items);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // POST /api/products
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto createdProduct = productService.createProduct(productRequestDto);
        ApiResponseDto<ProductResponseDto> response = new ApiResponseDto<>("Product created successfully", HttpStatus.CREATED.value(), createdProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/products/{productId}
    @PutMapping(value = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(productId, productRequestDto);
        ApiResponseDto<ProductResponseDto> response = new ApiResponseDto<>("Product updated successfully", HttpStatus.OK.value(), updatedProduct);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // DELETE /api/products/{productId}
    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
