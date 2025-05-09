package com.ivay.service;

import java.util.List;
import com.ivay.dtos.addressdto.AddressRequestDto;
import com.ivay.dtos.addressdto.AddressResponseDto;

public interface AddressService {
    List<AddressResponseDto> getAllAddresses();
    AddressResponseDto getAddressById(Long id);
    List<AddressResponseDto> getAddressesByUserId(Long userId);
    AddressResponseDto createAddress(AddressRequestDto addressRequestDto, String username);
    AddressResponseDto updateAddress(Long id, AddressRequestDto addressRequestDto, String username);
    void deleteAddress(Long id);
}