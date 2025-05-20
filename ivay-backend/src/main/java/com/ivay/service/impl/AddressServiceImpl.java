package com.ivay.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ivay.dtos.addressdto.AddressRequestDto;
import com.ivay.dtos.addressdto.AddressResponseDto;
import com.ivay.entity.Address;
import com.ivay.entity.UserEntity;
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
	
	public boolean isOwner(Long addressId, String username) {
	    return addressRepository.findById(addressId)
	      .map(address -> address.getUser().getName().equals(username))
	      .orElse(false);
	  }

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
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
        									   .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_SUPERADMIN"));

        if (!isAdmin && !address.getUser().getName().equals(currentUsername)) {
            throw new AccessDeniedException("You are not allowed to access this address");
        }

        return addressMapper.toAddressResponse(address);
    }
	
	
	@Override
	public List<AddressResponseDto> getAddressesByUserId(Long userId) {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));
		return user.getAddresses()
				.stream()
				.map(addressMapper::toAddressResponse)
				.collect(Collectors.toList());
	}

	@Override
	public AddressResponseDto createAddress(AddressRequestDto addressRequestDto, String username) {

		UserEntity current = userRepository.findUserEntityByName(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

		boolean isAdmin = current.getRole().getRoleName().equals("ADMIN") 
				|| current.getRole().getRoleName().equals("SUPERADMIN");

		if (!isAdmin && !current.getId().equals(addressRequestDto.getUserId())) {
			throw new AccessDeniedException("No tienes permiso para crear direcciones de otro usuario");
		}

		UserEntity user = userRepository.findById(addressRequestDto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User with id: " + addressRequestDto.getUserId() + " not found"));

		Address address = addressMapper.toAddress(addressRequestDto);
		address.setUser(user);
		Address savedAddress = addressRepository.save(address);

		return addressMapper.toAddressResponse(savedAddress);
	}

	@Override
	public AddressResponseDto updateAddress(Long id, AddressRequestDto addressRequestDto, String username) {

		UserEntity currentUser = userRepository.findUserEntityByName(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

		boolean isAdmin = currentUser.getRole().getRoleName().equals("ADMIN") || currentUser.getRole().getRoleName().equals("SUPERADMIN");

		if (!isAdmin && !currentUser.getId().equals(addressRequestDto.getUserId())) {
			throw new AccessDeniedException("No tienes permiso para modificar direcciones de otro usuario");
		}

		Address existingAddress = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address with id: " + id + " not found"));
		UserEntity user = userRepository.findById(addressRequestDto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User with id: " + addressRequestDto.getUserId() + " not found"));

		existingAddress.setUser(user);
		existingAddress.setAddress(addressRequestDto.getAddress());
		existingAddress.setZipCode(addressRequestDto.getZipCode());
		existingAddress.setProvince(addressRequestDto.getProvince());
		existingAddress.setLocality(addressRequestDto.getLocality());

		Address updatedAddress = addressRepository.save(existingAddress);
		return addressMapper.toAddressResponse(updatedAddress);
	}

	@Override
	public void deleteAddress(Long id) {
		Address address = addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address with id: " + id + " not found"));
		addressRepository.delete(address);
	}
}