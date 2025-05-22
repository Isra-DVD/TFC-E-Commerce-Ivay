package com.ivay.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;
import com.ivay.entity.UserEntity;

/**
 * Mapper interface for converting between {@link UserEntity} and its DTOs.
 *
 * Provides methods to:
 * - create a {@link UserEntity} from a {@link UserRequestDto}
 * - create a {@link UserResponseDto} from a {@link UserEntity}
 *
 * @since 1.0.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a {@link UserRequestDto} to a {@link UserEntity}.
     *
     * Ignores fields that are managed elsewhere:
     * - id is auto-generated
     * - role, orders, addresses, cartItems are set in service logic
     *
     * @param userRequestDto the DTO containing user data from the client
     * @return a new UserEntity populated with data from the DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    UserEntity toUser(UserRequestDto userRequestDto);

    /**
     * Maps a {@link UserEntity} to a {@link UserResponseDto}.
     *
     * Extracts the role identifier into the {@code roleId} field
     * and copies all other matching fields.
     *
     * @param user the entity retrieved from the database
     * @return a UserResponseDto containing data for API responses
     */
    @Mapping(source = "role.id", target = "roleId")
    UserResponseDto toUserResponse(UserEntity user);
}
