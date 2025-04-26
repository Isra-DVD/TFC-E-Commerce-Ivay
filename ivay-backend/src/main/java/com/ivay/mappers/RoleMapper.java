package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.roledto.RoleRequestDto;
import com.ivay.dtos.roledto.RoleResponseDto;
import com.ivay.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role toRole(RoleRequestDto roleRequestDto);

    RoleResponseDto toRoleResponse(Role role);
}
