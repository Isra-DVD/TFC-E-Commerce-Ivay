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

/**
 * Service implementation for {@link ProductService}.
 *
 * Provides CRUD operations for products, including pagination,
 * and retrieval of associated order and cart items.
 * Validates existence of related entities (category, supplier),
 * enforces business rules for deletion, and supports stock checks.
 *
 * All methods run within a transactional context and write logs
 * for key actions and warnings.
 *
 * @since 1.0.0
 */
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository     productRepository;
    private final ProductMapper         productMapper;
    private final CategoryRepository    categoryRepository;
    private final SupplierRepository    supplierRepository;
    private final OrderItemMapper       orderItemMapper;
    private final CartItemMapper        cartItemMapper;

    private static final String PRODUCT_NOT_FOUND   = "Product with id %d not found";
    private static final String CATEGORY_NOT_FOUND  = "Category with id %d not found (for product creation/update)";
    private static final String SUPPLIER_NOT_FOUND  = "Supplier with id %d not found (for product creation/update)";

    /**
     * Retrieves a {@link Product} by its id or throws if not found.
     *
     * @param productId the id of the product
     * @return the found {@code Product}
     * @throws ResourceNotFoundException if no product exists with the given id
     */
    private Product validateAndGetProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(PRODUCT_NOT_FOUND, productId)));
    }

    /**
     * Retrieves a {@link Category} by its id or null if id is null;
     * throws if id non-null and category not found.
     *
     * @param categoryId the id of the category
     * @return the found {@code Category}, or null if {@code categoryId} is null
     * @throws ResourceNotFoundException if {@code categoryId} non-null and not found
     */
    private Category validateAndGetCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(CATEGORY_NOT_FOUND, categoryId)));
    }

    /**
     * Retrieves a {@link Supplier} by its id or null if id is null;
     * throws if id non-null and supplier not found.
     *
     * @param supplierId the id of the supplier
     * @return the found {@code Supplier}, or null if {@code supplierId} is null
     * @throws ResourceNotFoundException if {@code supplierId} non-null and not found
     */
    private Supplier validateAndGetSupplier(Long supplierId) {
        if (supplierId == null) {
            return null;
        }
        return supplierRepository.findById(supplierId)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format(SUPPLIER_NOT_FOUND, supplierId)));
    }

    /**
     * {@inheritDoc}
     *
     * Retrieves all products.
     *
     * @return list of {@link ProductResponseDto}
     */
    @Override
    public List<ProductResponseDto> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll().stream()
            .map(productMapper::toProductResponse)
            .toList();
    }

    /**
     * {@inheritDoc}
     *
     * Finds products whose name contains the given substring, case-insensitive.
     *
     * @param name substring to search for
     * @return list of matching {@link ProductResponseDto}
     */
    @Override
    public List<ProductResponseDto> findProductsByName(String name) {
        log.info("Finding products with name containing: {}", name);
        return productRepository.findByNameContainingIgnoreCase(name).stream()
            .map(productMapper::toProductResponse)
            .toList();
    }

    /**
     * {@inheritDoc}
     *
     * Retrieves a product by its id.
     *
     * @param productId the id of the product
     * @return the corresponding {@link ProductResponseDto}
     * @throws ResourceNotFoundException if not found
     */
    @Override
    public ProductResponseDto getProductById(Long productId) {
        log.info("Fetching product with id: {}", productId);
        Product product = validateAndGetProduct(productId);
        return productMapper.toProductResponse(product);
    }

    /**
     * {@inheritDoc}
     *
     * Creates a new product, validating category and supplier if provided.
     *
     * @param dto the {@link ProductRequestDto} containing product data
     * @return created {@link ProductResponseDto}
     * @throws ResourceNotFoundException if category or supplier id invalid
     */
    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        log.info("Creating new product with name: {}", dto.getName());
        Product product = productMapper.toProduct(dto);
        product.setCategory(validateAndGetCategory(dto.getCategoryId()));
        product.setSupplier(validateAndGetSupplier(dto.getSupplierId()));

        Product saved = productRepository.save(product);
        log.info("Created product with id: {}", saved.getId());
        return productMapper.toProductResponse(saved);
    }

    /**
     * {@inheritDoc}
     *
     * Updates an existing product’s fields and associations.
     *
     * @param productId the id of the product to update
     * @param dto       the {@link ProductRequestDto} with new data
     * @return updated {@link ProductResponseDto}
     * @throws ResourceNotFoundException if product, category, or supplier not found
     */
    @Override
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto dto) {
        log.info("Updating product with id: {}", productId);
        Product product = validateAndGetProduct(productId);

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDiscount(dto.getDiscount());
        product.setImageUrl(dto.getImageUrl());

        if (!Objects.equals(
                product.getCategory() != null ? product.getCategory().getId() : null,
                dto.getCategoryId())) {
            log.info("Updating category for product {}", productId);
            product.setCategory(validateAndGetCategory(dto.getCategoryId()));
        }
        if (!Objects.equals(
                product.getSupplier() != null ? product.getSupplier().getId() : null,
                dto.getSupplierId())) {
            log.info("Updating supplier for product {}", productId);
            product.setSupplier(validateAndGetSupplier(dto.getSupplierId()));
        }

        Product updated = productRepository.save(product);
        log.info("Updated product with id: {}", updated.getId());
        return productMapper.toProductResponse(updated);
    }

    /**
     * {@inheritDoc}
     *
     * Deletes a product if it has no associated order or cart items.
     *
     * @param productId the id of the product to delete
     * @throws ResourceNotFoundException if not found
     * @throws IllegalStateException     if associations exist
     */
    @Override
    public void deleteProduct(Long productId) {
        log.info("Attempting to delete product with id: {}", productId);
        Product product = validateAndGetProduct(productId);

        if ((product.getOrderItems() != null && !product.getOrderItems().isEmpty())
         || (product.getCartItems()  != null && !product.getCartItems().isEmpty())) {
            log.warn("Cannot delete product {}: has existing associations", productId);
            throw new IllegalStateException(
                "Cannot delete product associated with orders or carts. Remove associations first.");
        }

        productRepository.delete(product);
        log.info("Deleted product with id: {}", productId);
    }

    /**
     * {@inheritDoc}
     *
     * Retrieves all order items for a specified product.
     *
     * @param productId the id of the product
     * @return list of {@link OrderItemResponseDto}
     * @throws ResourceNotFoundException if not found
     */
    @Override
    public List<OrderItemResponseDto> getOrderItemsByProductId(Long productId) {
        log.info("Fetching order items for product id: {}", productId);
        Product product = validateAndGetProduct(productId);
        return product.getOrderItems().stream()
            .map(orderItemMapper::toOrderItemResponse)
            .toList();
    }

    /**
     * {@inheritDoc}
     *
     * Retrieves all cart items for a specified product.
     *
     * @param productId the id of the product
     * @return list of {@link CartItemResponseDto}
     * @throws ResourceNotFoundException if not found
     */
    @Override
    public List<CartItemResponseDto> getCartItemsByProductId(Long productId) {
        log.info("Fetching cart items for product id: {}", productId);
        Product product = validateAndGetProduct(productId);
        return product.getCartItems().stream()
            .map(cartItemMapper::toCartItemResponse)
            .toList();
    }

    /**
     * {@inheritDoc}
     *
     * Retrieves a paginated page of products sorted by id.
     *
     * @param page zero-based page index
     * @param size number of items per page
     * @return {@link PaginatedProductResponseDto} containing content and metadata
     * @throws ResourceNotFoundException if requested page index is out of range
     */
    @Override
    public PaginatedProductResponseDto getProductsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Product> productPage = productRepository.findAll(pageable);

        if (page >= productPage.getTotalPages()) {
            throw new ResourceNotFoundException(
                String.format("Page %d out of range (total pages: %d)",
                              page, productPage.getTotalPages()));
        }

        List<ProductResponseDto> dtos = productPage.getContent().stream()
            .map(productMapper::toProductResponse)
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
