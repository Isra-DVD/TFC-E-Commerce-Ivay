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

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public List<SupplierResponseDto> getAllSuppliers() {
        return supplierRepository.findAll()
            .stream()
            .map(supplierMapper::toSupplierResponse)
            .collect(Collectors.toList());
    }

    @Override
    public SupplierResponseDto getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier with id: " + id + " not found"));
        return supplierMapper.toSupplierResponse(supplier);
    }

    @Override
    public SupplierResponseDto createSupplier(SupplierRequestDto supplierRequestDto) {
        Supplier supplier = supplierMapper.toSupplier(supplierRequestDto);
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toSupplierResponse(saved);
    }

    @Override
    public SupplierResponseDto updateSupplier(Long id, SupplierRequestDto supplierRequestDto) {
        Supplier existing = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier with id: " + id + " not found"));
        existing.setName(supplierRequestDto.getName());
        existing.setEmail(supplierRequestDto.getEmail());
        existing.setAddress(supplierRequestDto.getAddress());
        existing.setPhone(supplierRequestDto.getPhone());
        existing.setImageUrl(supplierRequestDto.getImageUrl());
        Supplier updated = supplierRepository.save(existing);
        return supplierMapper.toSupplierResponse(updated);
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier with id: " + id + " not found"));
        supplierRepository.delete(supplier);
    }
}
