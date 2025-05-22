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

/**
 * Service implementation for managing addresses.
 *
 * Provides methods to
 * - retrieve all addresses
 * - retrieve, create, update and delete an address
 * - enforce ownership and role-based access control
 *
 * @since 1.0.0
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepository userRepository;

    /**
     * Checks if the given username is the owner of the address.
     *
     * @param addressId the identifier of the address
     * @param username  the username to check ownership for
     * @return true if the user owns the address, false otherwise
     */
    public boolean isOwner(Long addressId, String username) {
        return addressRepository.findById(addressId)
            .map(address -> address.getUser().getName().equals(username))
            .orElse(false);
    }

    /**
     * Retrieves all addresses in the system.
     *
     * @return list of AddressResponseDto representing all addresses
     */
    @Override
    public List<AddressResponseDto> getAllAddresses() {
        return addressRepository.findAll()
            .stream()
            .map(addressMapper::toAddressResponse)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves an address by its identifier with access control.
     *
     * Allows access if the caller is an ADMIN/SUPERADMIN or the owner.
     *
     * @param id the identifier of the address to retrieve
     * @return the AddressResponseDto for the specified address
     * @throws ResourceNotFoundException if no address is found
     * @throws AccessDeniedException     if the caller is not authorized
     */
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

    /**
     * Retrieves all addresses for a specific user.
     *
     * @param userId the identifier of the user
     * @return list of AddressResponseDto for that user
     * @throws ResourceNotFoundException if the user is not found
     */
    @Override
    public List<AddressResponseDto> getAddressesByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + userId + " not found"));
        return user.getAddresses()
            .stream()
            .map(addressMapper::toAddressResponse)
            .collect(Collectors.toList());
    }

    /**
     * Creates a new address for a user with access control.
     *
     * Allows ADMIN/SUPERADMIN or the owner to create.
     *
     * @param addressRequestDto the data for the new address
     * @param username          the username performing the operation
     * @return the created AddressResponseDto
     * @throws ResourceNotFoundException if the user is not found
     * @throws AccessDeniedException     if the caller is not authorized
     */
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

    /**
     * Updates an existing address with access control.
     *
     * Allows ADMIN/SUPERADMIN or the owner to update.
     *
     * @param id                the identifier of the address to update
     * @param addressRequestDto the new data for the address
     * @param username          the username performing the operation
     * @return the updated AddressResponseDto
     * @throws ResourceNotFoundException if address or user is not found
     * @throws AccessDeniedException     if the caller is not authorized
     */
    @Override
    public AddressResponseDto updateAddress(Long id, AddressRequestDto addressRequestDto, String username) {
        UserEntity currentUser = userRepository.findUserEntityByName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        boolean isAdmin = currentUser.getRole().getRoleName().equals("ADMIN")
            || currentUser.getRole().getRoleName().equals("SUPERADMIN");
        if (!isAdmin && !currentUser.getId().equals(addressRequestDto.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para modificar direcciones de otro usuario");
        }

        Address existingAddress = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address with id: " + id + " not found"));

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

    /**
     * Deletes an address by its identifier.
     *
     * @param id the identifier of the address to delete
     * @throws ResourceNotFoundException if no address is found
     */
    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address with id: " + id + " not found"));
        addressRepository.delete(address);
    }
}
