package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.supplierdto.SupplierRequestDto;
import com.ivay.dtos.supplierdto.SupplierResponseDto;
import com.ivay.entity.Supplier;



@Mapper(componentModel = "spring")
public interface SupplierMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true) 
    Supplier toSupplier(SupplierRequestDto supplierRequestDto);

    SupplierResponseDto toSupplierResponse(Supplier supplier);
}
