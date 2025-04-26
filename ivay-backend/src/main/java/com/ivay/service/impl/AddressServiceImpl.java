package com.ivay.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivay.dtos.addressdto.AddressRequestDto;
import com.ivay.dtos.addressdto.AddressResponseDto;
import com.ivay.entity.Address;
import com.ivay.entity.User;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.AddressMapper;
import com.ivay.repository.AddressRepository;
import com.ivay.repository.UserRepository;
import com.ivay.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AddressResponseDto> getAllAddresses() {
        return addressRepository.findAll()
            .stream()
            .map(addressMapper::toAddressResponse)
            .collect(Collectors.toList());
    }

    @Override
    public AddressResponseDto getAddressById(Long id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address with id: " + id + " not found"));
        return addressMapper.toAddressResponse(address);
    }

    @Override
    public List<AddressResponseDto> getAddressesByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));
        return user.getAddresses()
            .stream()
            .map(addressMapper::toAddressResponse)
            .collect(Collectors.toList());
    }

    @Override
    public AddressResponseDto createAddress(AddressRequestDto addressRequestDto) {
        User user = userRepository.findById(addressRequestDto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + addressRequestDto.getUserId() + " not found"));
        Address address = addressMapper.toAddress(addressRequestDto);
        address.setUser(user);
        Address saved = addressRepository.save(address);
        return addressMapper.toAddressResponse(saved);
    }

    @Override
    public AddressResponseDto updateAddress(Long id, AddressRequestDto addressRequestDto) {
        Address existing = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address with id: " + id + " not found"));
        User user = userRepository.findById(addressRequestDto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + addressRequestDto.getUserId() + " not found"));
        existing.setUser(user);
        existing.setAddress(addressRequestDto.getAddress());
        Address updated = addressRepository.save(existing);
        return addressMapper.toAddressResponse(updated);
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address with id: " + id + " not found"));
        addressRepository.delete(address);
    }
}