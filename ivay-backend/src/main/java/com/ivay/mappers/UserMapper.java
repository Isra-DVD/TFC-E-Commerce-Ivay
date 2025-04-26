package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;
import com.ivay.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    User toUser(UserRequestDto userRequestDto);

    @Mapping(source = "role.id", target = "roleId")
    UserResponseDto toUserResponse(User user);
}