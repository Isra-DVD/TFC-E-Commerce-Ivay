package com.ivay.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivay.dtos.supplierdto.SupplierRequestDto;
import com.ivay.dtos.supplierdto.SupplierResponseDto;
import com.ivay.entity.Supplier;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.SupplierMapper;
import com.ivay.repository.SupplierRepository;
import com.ivay.service.SupplierService;

/**
 * Implementation of {@link SupplierService} for managing supplier data.
 *
 * Provides methods to create, read, update, and delete suppliers.
 *
 * @since 1.0.0
 */
@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * Retrieves all suppliers in the system.
     *
     * @return a list of {@link SupplierResponseDto} representing every supplier
     */
    @Override
    public List<SupplierResponseDto> getAllSuppliers() {
        return supplierRepository.findAll().stream()
            .map(supplierMapper::toSupplierResponse)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a supplier by its identifier.
     *
     * @param id the identifier of the supplier to retrieve
     * @return the corresponding {@link SupplierResponseDto}
     * @throws ResourceNotFoundException if no supplier exists with the given id
     */
    @Override
    public SupplierResponseDto getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Supplier with id: " + id + " not found"));
        return supplierMapper.toSupplierResponse(supplier);
    }

    /**
     * Creates a new supplier based on the provided data.
     *
     * @param supplierRequestDto the data transfer object containing supplier details
     * @return the created {@link SupplierResponseDto}
     */
    @Override
    public SupplierResponseDto createSupplier(SupplierRequestDto supplierRequestDto) {
        Supplier supplier = supplierMapper.toSupplier(supplierRequestDto);
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(saved);
    }

    /**
     * Updates an existing supplier's details.
     *
     * @param id the identifier of the supplier to update
     * @param supplierRequestDto the data transfer object containing updated details
     * @return the updated {@link SupplierResponseDto}
     * @throws ResourceNotFoundException if no supplier exists with the given id
     */
    @Override
    public SupplierResponseDto updateSupplier(Long id, SupplierRequestDto supplierRequestDto) {
        Supplier existing = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Supplier with id: " + id + " not found"));
        existing.setName(supplierRequestDto.getName());
        existing.setEmail(supplierRequestDto.getEmail());
        existing.setAddress(supplierRequestDto.getAddress());
        existing.setPhone(supplierRequestDto.getPhone());
        existing.setImageUrl(supplierRequestDto.getImageUrl());
        Supplier updated = supplierRepository.save(existing);
        return supplierMapper.toSupplierResponse(updated);
    }

    /**
     * Deletes a supplier by its identifier.
     *
     * @param id the identifier of the supplier to delete
     * @throws ResourceNotFoundException if no supplier exists with the given id
     */
    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Supplier with id: " + id + " not found"));
        supplierRepository.delete(supplier);
    }
}
