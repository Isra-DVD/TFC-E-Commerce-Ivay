package com.ivay.service;

import java.util.List;
import com.ivay.dtos.supplierdto.SupplierRequestDto;
import com.ivay.dtos.supplierdto.SupplierResponseDto;

/**
 * Service interface for managing suppliers.
 *
 * Provides methods to
 * - retrieve all suppliers
 * - retrieve a supplier by its identifier
 * - create a new supplier
 * - update an existing supplier
 * - delete a supplier
 *
 * @since 1.0.0
 */
public interface SupplierService {

    /**
     * Retrieves all suppliers in the system.
     *
     * @return a list of {@link SupplierResponseDto} representing all suppliers
     */
    List<SupplierResponseDto> getAllSuppliers();

    /**
     * Retrieves a single supplier by its identifier.
     *
     * @param id the identifier of the supplier to retrieve
     * @return the {@link SupplierResponseDto} for the given id
     */
    SupplierResponseDto getSupplierById(Long id);

    /**
     * Creates a new supplier.
     *
     * @param supplierRequestDto the data for the new supplier
     * @return the created {@link SupplierResponseDto}
     */
    SupplierResponseDto createSupplier(SupplierRequestDto supplierRequestDto);

    /**
     * Updates an existing supplier.
     *
     * @param id the identifier of the supplier to update
     * @param supplierRequestDto the new data for the supplier
     * @return the updated {@link SupplierResponseDto}
     */
    SupplierResponseDto updateSupplier(Long id, SupplierRequestDto supplierRequestDto);

    /**
     * Deletes a supplier by its identifier.
     *
     * @param id the identifier of the supplier to delete
     */
    void deleteSupplier(Long id);
}
