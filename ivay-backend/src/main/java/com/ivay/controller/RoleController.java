package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.roledto.RoleRequestDto;
import com.ivay.dtos.roledto.RoleResponseDto;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<RoleResponseDto>>> getAllRoles() {
        List<RoleResponseDto> roles = roleService.getAllRoles();
        ApiResponseDto<List<RoleResponseDto>> response = new ApiResponseDto<>("Roles fetched successfully", HttpStatus.OK.value(), roles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<RoleResponseDto>> getRoleById(@PathVariable Long id) {
        RoleResponseDto role = roleService.getRoleById(id);
        ApiResponseDto<RoleResponseDto> response = new ApiResponseDto<>("Role fetched successfully", HttpStatus.OK.value(), role);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<RoleResponseDto>> createRole(
            @Valid @RequestBody RoleRequestDto roleRequestDto) {
        RoleResponseDto created = roleService.createRole(roleRequestDto);
        ApiResponseDto<RoleResponseDto> response = new ApiResponseDto<>("Role created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<RoleResponseDto>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDto roleRequestDto) {
        RoleResponseDto updated = roleService.updateRole(id, roleRequestDto);
        ApiResponseDto<RoleResponseDto> response = new ApiResponseDto<>("Role updated successfully", HttpStatus.OK.value(), updated);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

