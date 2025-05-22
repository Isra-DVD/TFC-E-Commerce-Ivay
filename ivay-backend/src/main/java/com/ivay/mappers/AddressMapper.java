package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.addressdto.AddressRequestDto;
import com.ivay.dtos.addressdto.AddressResponseDto;
import com.ivay.entity.Address;

/**
 * Mapper interface for converting between {@link Address} entities and their DTOs.
 *
 * Defines methods to:
 * - Create an {@link Address} entity from an {@link AddressRequestDto}.
 * - Create an {@link AddressResponseDto} from an {@link Address} entity.
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface AddressMapper {

    /**
     * Maps an {@link AddressRequestDto} to an {@link Address} entity.
     *
     * Ignores the {@code id} and {@code user} fields on the entity, as they
     * will be set elsewhere (e.g., by the persistence layer and service logic).
     *
     * @param addressRequestDto the DTO containing address data from the client
     * @return a new {@code Address} entity populated with data from the DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toAddress(AddressRequestDto addressRequestDto);

    /**
     * Maps an {@link Address} entity to an {@link AddressResponseDto}.
     *
     * Copies the entity's fields into the response DTO and extracts the
     * associated user's identifier into the {@code userId} field.
     *
     * @param address the entity retrieved from the database
     * @return an {@code AddressResponseDto} containing data for API responses
     */
    @Mapping(source = "user.id", target = "userId")
    AddressResponseDto toAddressResponse(Address address);
}
