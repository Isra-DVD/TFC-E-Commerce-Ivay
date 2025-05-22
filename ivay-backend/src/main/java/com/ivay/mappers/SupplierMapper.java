package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.supplierdto.SupplierRequestDto;
import com.ivay.dtos.supplierdto.SupplierResponseDto;
import com.ivay.entity.Supplier;

/**
 * Mapper interface for converting between Supplier entities and their DTOs.
 *
 * Provides methods to:
 * - create a Supplier entity from a SupplierRequestDto
 * - create a SupplierResponseDto from a Supplier entity
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface SupplierMapper {

    /**
     * Maps a SupplierRequestDto to a Supplier entity.
     *
     * Ignores the id and products fields on the entity, since the id is
     * auto-generated and product associations are managed elsewhere.
     *
     * @param supplierRequestDto the DTO containing supplier data from the client
     * @return a new Supplier entity populated with data from the DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    Supplier toSupplier(SupplierRequestDto supplierRequestDto);

    /**
     * Maps a Supplier entity to a SupplierResponseDto.
     *
     * Copies all matching fields from the entity into the response DTO.
     *
     * @param supplier the entity retrieved from the database
     * @return a SupplierResponseDto containing data for API responses
     */
    SupplierResponseDto toSupplierResponse(Supplier supplier);
}
