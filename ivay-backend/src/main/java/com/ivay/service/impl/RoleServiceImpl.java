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

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll()
            .stream()
            .map(roleMapper::toRoleResponse)
            .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + id + " not found"));
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
        Role role = Role.builder()
        				.roleName(roleRequestDto.getRoleName())
        				.build();
        Role savedRole = roleRepository.save(role);
        return roleMapper.toRoleResponse(savedRole);
    }

    @Override
    public RoleResponseDto updateRole(Long id, RoleRequestDto roleRequestDto) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + id + " not found"));
        role.setRoleName(roleRequestDto.getRoleName());
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toRoleResponse(updatedRole);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + id + " not found"));
        roleRepository.delete(role);
    }
}

