package com.ivay.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivay.dtos.roledto.RoleRequestDto;
import com.ivay.dtos.roledto.RoleResponseDto;
import com.ivay.entity.Role;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.RoleMapper;
import com.ivay.repository.RoleRepository;
import com.ivay.service.RoleService;

/**
 * Implementation of {@link RoleService} for managing security roles.
 *
 * This service provides methods to retrieve, create, update, and delete roles.
 * It uses {@link RoleRepository} for persistence operations and {@link RoleMapper}
 * to convert between {@link Role} entities and their DTO representations.
 *
 * @since 1.0.0
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private RoleMapper roleMapper;

    /**
     * Retrieves all roles in the system.
     *
     * @return a list of {@link RoleResponseDto} representing every role
     */
    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll()
                             .stream()
                             .map(roleMapper::toRoleResponse)
                             .collect(Collectors.toList());
    }

    /**
     * Retrieves a single role by its identifier.
     *
     * @param id the identifier of the role to fetch
     * @return the corresponding {@link RoleResponseDto}
     * @throws ResourceNotFoundException if no role exists with the given id
     */
    @Override
    public RoleResponseDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + id + " not found"));
        return roleMapper.toRoleResponse(role);
    }

    /**
     * Creates a new role based on the provided data.
     *
     * @param roleRequestDto the DTO containing the name of the new role
     * @return the created {@link RoleResponseDto}
     */
    @Override
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
        Role role = Role.builder()
                        .roleName(roleRequestDto.getRoleName())
                        .build();
        Role savedRole = roleRepository.save(role);
        return roleMapper.toRoleResponse(savedRole);
    }

    /**
     * Updates the name of an existing role.
     *
     * @param id the identifier of the role to update
     * @param roleRequestDto the DTO containing the new role name
     * @return the updated {@link RoleResponseDto}
     * @throws ResourceNotFoundException if no role exists with the given id
     */
    @Override
    public RoleResponseDto updateRole(Long id, RoleRequestDto roleRequestDto) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + id + " not found"));
        role.setRoleName(roleRequestDto.getRoleName());
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toRoleResponse(updatedRole);
    }

    /**
     * Deletes a role by its identifier.
     *
     * @param id the identifier of the role to delete
     * @throws ResourceNotFoundException if no role exists with the given id
     */
    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + id + " not found"));
        roleRepository.delete(role);
    }
}
