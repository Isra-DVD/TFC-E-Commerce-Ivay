package com.ivay.service;

import java.util.List;

import com.ivay.dtos.userdto.ChangePasswordRequestDto;
import com.ivay.dtos.userdto.UpdateProfileRequestDto;
import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;

public interface UserEntityService {
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto getByUsername(String username);
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);
    UserResponseDto updateProfile(String username, UpdateProfileRequestDto dto);
    void changePassword(String username, ChangePasswordRequestDto dto);
}
