package com.ivay.mappers.address;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.addressdto.AddressRequestDto;
import com.ivay.dtos.addressdto.AddressResponseDto;
import com.ivay.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) 
    Address toAddress(AddressRequestDto addressRequestDto);

    @Mapping(source = "user.id", target = "userId")
    AddressResponseDto toAddressResponse(Address address);
}
