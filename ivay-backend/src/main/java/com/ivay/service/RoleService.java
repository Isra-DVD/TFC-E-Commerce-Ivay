package com.ivay.service;

import java.util.List;
import com.ivay.dtos.roledto.RoleRequestDto;
import com.ivay.dtos.roledto.RoleResponseDto;

/**
 * Service interface for managing roles.
 *
 * Provides methods to:
 * - retrieve all roles
 * - retrieve a role by its identifier
 * - create a new role
 * - update an existing role
 * - delete a role
 *
 * @since 1.0.0
 */
public interface RoleService {

    /**
     * Retrieves all roles in the system.
     *
     * @return a list of {@link RoleResponseDto} representing all roles
     */
    List<RoleResponseDto> getAllRoles();

    /**
     * Retrieves a single role by its identifier.
     *
     * @param id the identifier of the role to retrieve
     * @return the {@link RoleResponseDto} for the given id
     */
    RoleResponseDto getRoleById(Long id);

    /**
     * Creates a new role.
     *
     * @param roleRequestDto the data for the new role
     * @return the created {@link RoleResponseDto}
     */
    RoleResponseDto createRole(RoleRequestDto roleRequestDto);

    /**
     * Updates an existing role.
     *
     * @param id the identifier of the role to update
     * @param roleRequestDto the new data for the role
     * @return the updated {@link RoleResponseDto}
     */
    RoleResponseDto updateRole(Long id, RoleRequestDto roleRequestDto);

    /**
     * Deletes a role by its identifier.
     *
     * @param id the identifier of the role to delete
     */
    void deleteRole(Long id);
}
