package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.roledto.RoleRequestDto;
import com.ivay.dtos.roledto.RoleResponseDto;
import com.ivay.entity.Role;

/**
 * Mapper interface for converting between Role entities and their DTOs.
 *
 * Provides methods to:
 * - create a {@link Role} entity from a {@link RoleRequestDto}
 * - create a {@link RoleResponseDto} from a {@link Role} entity
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    /**
     * Maps a {@link RoleRequestDto} to a {@link Role} entity.
     *
     * Ignores the id and users fields on the entity, since the id is auto-generated
     * and user associations are managed separately.
     *
     * @param roleRequestDto the DTO containing role data from the client
     * @return a new Role entity populated with data from the DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "users", ignore = true)
    Role toRole(RoleRequestDto roleRequestDto);

    /**
     * Maps a {@link Role} entity to a {@link RoleResponseDto}.
     *
     * Copies the role's identifier and name into the response DTO.
     *
     * @param role the entity retrieved from the database
     * @return a RoleResponseDto containing data for API responses
     */
    RoleResponseDto toRoleResponse(Role role);
}
