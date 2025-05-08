package com.ivay.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ivay.dtos.cartitemdto.CartItemResponseDto;
import com.ivay.dtos.orderitemdto.OrderItemResponseDto;
import com.ivay.dtos.productdto.PaginatedProductResponseDto;
import com.ivay.dtos.productdto.ProductRequestDto;
import com.ivay.dtos.productdto.ProductResponseDto;
import com.ivay.entity.Category;
import com.ivay.entity.Product;
import com.ivay.entity.Supplier;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.CartItemMapper;
import com.ivay.mappers.OrderItemMapper;
import com.ivay.mappers.ProductMapper;
import com.ivay.repository.CategoryRepository;
import com.ivay.repository.ProductRepository;
import com.ivay.repository.SupplierRepository;
import com.ivay.service.ProductService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final CategoryRepository categoryRepository;
	private final SupplierRepository supplierRepository;
	private final OrderItemMapper orderItemMapper;
	private final CartItemMapper cartItemMapper;

	private static final String PRODUCT_NOT_FOUND = "Product with id %d not found";
	private static final String CATEGORY_NOT_FOUND = "Category with id %d not found (for product creation/update)";
	private static final String SUPPLIER_NOT_FOUND = "Supplier with id %d not found (for product creation/update)";

	private Product validateAndGetProduct(Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, productId)));
	}

	private Category validateAndGetCategory(Long categoryId) {
		if (categoryId == null)
			return null;
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(CATEGORY_NOT_FOUND, categoryId)));
	}

	private Supplier validateAndGetSupplier(Long supplierId) {
		if (supplierId == null)
			return null;
		return supplierRepository.findById(supplierId)
				.orElseThrow(() -> new ResourceNotFoundException(String.format(SUPPLIER_NOT_FOUND, supplierId)));
	}

	@Override
	public List<ProductResponseDto> getAllProducts() {
		log.info("Fetching all products");
		return productRepository.findAll().stream().map(productMapper::toProductResponse).toList();
	}

	@Override
	public List<ProductResponseDto> findProductsByName(String name) {
		log.info("Finding products with name containing: {}", name);
		return productRepository.findByNameContainingIgnoreCase(name).stream().map(productMapper::toProductResponse)
				.toList();
	}

	@Override
	public ProductResponseDto getProductById(Long productId) {
		log.info("Fetching product with id: {}", productId);
		Product product = validateAndGetProduct(productId);
		return productMapper.toProductResponse(product);
	}

	@Override
	public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
		log.info("Creating new product with name: {}", productRequestDto.getName());
		Product product = productMapper.toProduct(productRequestDto);

		Category category = validateAndGetCategory(productRequestDto.getCategoryId());
		Supplier supplier = validateAndGetSupplier(productRequestDto.getSupplierId());
		product.setCategory(category);
		product.setSupplier(supplier);

		Product savedProduct = productRepository.save(product);
		log.info("Created product with id: {}", savedProduct.getId());
		return productMapper.toProductResponse(savedProduct);
	}

	@Override
	public ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto) {
		log.info("Updating product with id: {}", productId);
		Product product = validateAndGetProduct(productId);

		product.setName(productRequestDto.getName());
		product.setDescription(productRequestDto.getDescription());
		product.setPrice(productRequestDto.getPrice());
		product.setStock(productRequestDto.getStock());
		product.setDiscount(productRequestDto.getDiscount());

		if (!Objects.equals(product.getCategory() != null ? product.getCategory().getId() : null,
				productRequestDto.getCategoryId())) {
			log.info("Updating category for product {}", productId);
			Category category = validateAndGetCategory(productRequestDto.getCategoryId());
			product.setCategory(category);
		}
		if (!Objects.equals(product.getSupplier() != null ? product.getSupplier().getId() : null,
				productRequestDto.getSupplierId())) {
			log.info("Updating supplier for product {}", productId);
			Supplier supplier = validateAndGetSupplier(productRequestDto.getSupplierId());
			product.setSupplier(supplier);
		}

		Product updatedProduct = productRepository.save(product);
		log.info("Updated product with id: {}", updatedProduct.getId());
		return productMapper.toProductResponse(updatedProduct);
	}

	@Override
	public void deleteProduct(Long productId) {
		log.info("Attempting to delete product with id: {}", productId);
		Product product = validateAndGetProduct(productId);

		if ((product.getOrderItems() != null && !product.getOrderItems().isEmpty())
				|| (product.getCartItems() != null && !product.getCartItems().isEmpty())) {
			log.warn("Deletion failed: Product {} is associated with orders or carts.", productId);
			throw new IllegalStateException(
					"Cannot delete product associated with existing orders or carts. Please remove associations first.");
		}

		productRepository.delete(product);
		log.info("Deleted product with id: {}", productId);
	}

	@Override
	public List<OrderItemResponseDto> getOrderItemsByProductId(Long productId) {
		log.info("Fetching order items for product id: {}", productId);
		Product product = validateAndGetProduct(productId);
		return product.getOrderItems().stream().map(orderItemMapper::toOrderItemResponse).toList();
	}

	@Override
	public List<CartItemResponseDto> getCartItemsByProductId(Long productId) {
		log.info("Fetching cart items for product id: {}", productId);
		Product product = validateAndGetProduct(productId);
		return product.getCartItems().stream().map(cartItemMapper::toCartItemResponse).toList();
	}

	@Override
	public PaginatedProductResponseDto getProductsPaginated(int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("id"));

		Page<Product> productPage = productRepository.findAll(pageable);

		if (page >= productPage.getTotalPages()) {
			throw new ResourceNotFoundException(
					String.format("Page %d out of range (total pages: %d)", page, productPage.getTotalPages()));
		}

		List<ProductResponseDto> dtos = productPage.getContent().stream().map(productMapper::toProductResponse)
				.collect(Collectors.toList());

		PaginatedProductResponseDto result = new PaginatedProductResponseDto();
		result.setContent(dtos);
		result.setPage(productPage.getNumber());
		result.setSize(productPage.getSize());
		result.setTotalElements(productPage.getTotalElements());
		result.setTotalPages(productPage.getTotalPages());
		result.setHasNext(productPage.hasNext());

		return result;
	}
}
