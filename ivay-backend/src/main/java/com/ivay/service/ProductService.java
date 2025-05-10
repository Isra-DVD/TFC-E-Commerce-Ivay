package com.ivay.service;

import java.util.List;

import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.dtos.productdto.PaginatedProductResponseDto;
import com.ivay.dtos.productdto.ProductRequestDto;
import com.ivay.dtos.productdto.ProductResponseDto;

public interface ProductService {
	List<ProductResponseDto> getAllProducts();

	PaginatedProductResponseDto getProductsPaginated(int page, int size);

	List<ProductResponseDto> findProductsByName(String name);

	ProductResponseDto getProductById(Long productId);

	ProductResponseDto createProduct(ProductRequestDto productRequestDto);

	ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto);

	void deleteProduct(Long productId);

	List<OrderItemResponseDto> getOrderItemsByProductId(Long productId);

	List<CartItemResponseDto> getCartItemsByProductId(Long productId);
}
