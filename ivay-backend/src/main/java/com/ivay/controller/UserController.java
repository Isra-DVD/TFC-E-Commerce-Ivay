package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> list = userService.getAllUsers();
        ApiResponseDto<List<UserResponseDto>> response = new ApiResponseDto<>("Users fetched successfully", HttpStatus.OK.value(), list);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(@PathVariable Long id) {
        UserResponseDto dto = userService.getUserById(id);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User fetched successfully", HttpStatus.OK.value(), dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<UserResponseDto>> createUser(@Valid @RequestBody UserRequestDto dto) {
        UserResponseDto created = userService.createUser(dto);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto dto) {
        UserResponseDto updated = userService.updateUser(id, dto);
        ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User updated successfully", HttpStatus.OK.value(), updated);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
