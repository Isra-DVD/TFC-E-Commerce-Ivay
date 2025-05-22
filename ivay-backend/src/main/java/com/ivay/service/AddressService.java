package com.ivay.service;

import java.util.List;
import com.ivay.dtos.addressdto.AddressRequestDto;
import com.ivay.dtos.addressdto.AddressResponseDto;

/**
 * Service interface for managing addresses.
 *
 * Provides methods to:
 * - retrieve all addresses or by specific criteria
 * - create, update, and delete address records
 *
 * Implementations should enforce business rules such as
 * ownership checks and validation of input data.
 *
 * @since 1.0.0
 */
public interface AddressService {

    /**
     * Retrieves all addresses in the system.
     *
     * @return a list of {@link AddressResponseDto} representing all stored addresses
     */
    List<AddressResponseDto> getAllAddresses();

    /**
     * Retrieves a single address by its identifier.
     *
     * @param id the identifier of the address to retrieve
     * @return the {@link AddressResponseDto} for the given id
     */
    AddressResponseDto getAddressById(Long id);

    /**
     * Retrieves all addresses associated with a given user.
     *
     * @param userId the identifier of the user whose addresses to fetch
     * @return a list of {@link AddressResponseDto} for that user
     */
    List<AddressResponseDto> getAddressesByUserId(Long userId);

    /**
     * Creates a new address on behalf of a user.
     *
     * @param addressRequestDto the data for the new address
     * @param username the username performing the operation (for ownership or audit)
     * @return the created {@link AddressResponseDto}
     */
    AddressResponseDto createAddress(AddressRequestDto addressRequestDto, String username);

    /**
     * Updates an existing address.
     *
     * @param id the identifier of the address to update
     * @param addressRequestDto the new data for the address
     * @param username the username performing the operation (for ownership or audit)
     * @return the updated {@link AddressResponseDto}
     */
    AddressResponseDto updateAddress(Long id, AddressRequestDto addressRequestDto, String username);

    /**
     * Deletes an address by its identifier.
     *
     * @param id the identifier of the address to delete
     */
    void deleteAddress(Long id);
}
