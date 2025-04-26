package com.ivay.service;

import java.util.List;
import com.ivay.dtos.supplierdto.SupplierRequestDto;
import com.ivay.dtos.supplierdto.SupplierResponseDto;

public interface SupplierService {
    List<SupplierResponseDto> getAllSuppliers();
    SupplierResponseDto getSupplierById(Long id);
    SupplierResponseDto createSupplier(SupplierRequestDto supplierRequestDto);
    SupplierResponseDto updateSupplier(Long id, SupplierRequestDto supplierRequestDto);
    void deleteSupplier(Long id);
}