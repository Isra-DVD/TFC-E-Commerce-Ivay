package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.addressdto.AddressRequestDto;
import com.ivay.dtos.addressdto.AddressResponseDto;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:5173/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<AddressResponseDto>>> getAll() {
        List<AddressResponseDto> list = addressService.getAllAddresses();
        ApiResponseDto<List<AddressResponseDto>> response = new ApiResponseDto<>("Addresses fetched successfully", HttpStatus.OK.value(), list);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<AddressResponseDto>> getById(@PathVariable Long id) {
        AddressResponseDto dto = addressService.getAddressById(id);
        ApiResponseDto<AddressResponseDto> response = new ApiResponseDto<>("Address fetched successfully", HttpStatus.OK.value(), dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponseDto<List<AddressResponseDto>>> getByUser(@PathVariable Long userId) {
        List<AddressResponseDto> list = addressService.getAddressesByUserId(userId);
        ApiResponseDto<List<AddressResponseDto>> response = new ApiResponseDto<>("Addresses fetched successfully", HttpStatus.OK.value(), list);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<AddressResponseDto>> create(@Valid @RequestBody AddressRequestDto dto) {
        AddressResponseDto created = addressService.createAddress(dto);
        ApiResponseDto<AddressResponseDto> response = new ApiResponseDto<>("Address created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<AddressResponseDto>> update(@PathVariable Long id, @Valid @RequestBody AddressRequestDto dto) {
        AddressResponseDto updated = addressService.updateAddress(id, dto);
        ApiResponseDto<AddressResponseDto> response = new ApiResponseDto<>("Address updated successfully", HttpStatus.OK.value(), updated);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
