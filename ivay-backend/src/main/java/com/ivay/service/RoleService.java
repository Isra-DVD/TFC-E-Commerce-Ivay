package com.ivay.service;

import java.util.List;
import com.ivay.dtos.roledto.RoleRequestDto;
import com.ivay.dtos.roledto.RoleResponseDto;

public interface RoleService {
    List<RoleResponseDto> getAllRoles();
    RoleResponseDto getRoleById(Long id);
    RoleResponseDto createRole(RoleRequestDto roleRequestDto);
    RoleResponseDto updateRole(Long id, RoleRequestDto roleRequestDto);
    void deleteRole(Long id);
}