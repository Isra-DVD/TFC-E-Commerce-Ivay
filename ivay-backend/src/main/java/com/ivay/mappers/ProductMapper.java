package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.productdto.ProductRequestDto;
import com.ivay.dtos.productdto.ProductResponseDto;
import com.ivay.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "supplier", ignore = true) 
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Product toProduct(ProductRequestDto productRequestDto);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "supplier.id", target = "supplierId")
    ProductResponseDto toProductResponse(Product product);
}
