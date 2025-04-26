package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.supplierdto.SupplierRequestDto;
import com.ivay.dtos.supplierdto.SupplierResponseDto;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.SupplierService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<SupplierResponseDto>>> getAllSuppliers() {
        List<SupplierResponseDto> list = supplierService.getAllSuppliers();
        ApiResponseDto<List<SupplierResponseDto>> response = new ApiResponseDto<>("Suppliers fetched successfully", HttpStatus.OK.value(), list);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<SupplierResponseDto>> getSupplierById(@PathVariable Long id) {
        SupplierResponseDto dto = supplierService.getSupplierById(id);
        ApiResponseDto<SupplierResponseDto> response = new ApiResponseDto<>("Supplier fetched successfully", HttpStatus.OK.value(), dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<SupplierResponseDto>> createSupplier(@Valid @RequestBody SupplierRequestDto dto) {
        SupplierResponseDto created = supplierService.createSupplier(dto);
        ApiResponseDto<SupplierResponseDto> response = new ApiResponseDto<>("Supplier created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<SupplierResponseDto>> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierRequestDto dto) {
        SupplierResponseDto updated = supplierService.updateSupplier(id, dto);
        ApiResponseDto<SupplierResponseDto> response = new ApiResponseDto<>("Supplier updated successfully", HttpStatus.OK.value(), updated);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
