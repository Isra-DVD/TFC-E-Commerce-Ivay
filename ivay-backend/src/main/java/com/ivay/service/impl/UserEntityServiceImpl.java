package com.ivay.service.impl;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;
import com.ivay.entity.Role;
import com.ivay.entity.UserEntity;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.UserMapper;
import com.ivay.repository.UserRepository;
import com.ivay.service.UserEntityService;
import com.ivay.repository.RoleRepository;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(userMapper::toUserResponse)
            .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        Role role = roleRepository.findById(userRequestDto.getRoleId())
            .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + userRequestDto.getRoleId() + " not found"));
        UserEntity user = userMapper.toUser(userRequestDto);
        user.setRole(role);
        UserEntity saved = userRepository.save(user);
        return userMapper.toUserResponse(saved);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        UserEntity existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
        Role role = roleRepository.findById(userRequestDto.getRoleId())
            .orElseThrow(() -> new ResourceNotFoundException("Role with id: " + userRequestDto.getRoleId() + " not found"));

        existingUser.setName(userRequestDto.getName());
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setPassword(userRequestDto.getPassword());
        existingUser.setPhone(userRequestDto.getPhone());
        existingUser.setIsEnabled(userRequestDto.getIsEnabled());
        existingUser.setAccountNoExpired(userRequestDto.getAccountNoExpired());
        existingUser.setAccountNoLocked(userRequestDto.getAccountNoLocked());
        existingUser.setCredentialNoExpired(userRequestDto.getCredentialNoExpired());
        existingUser.setRole(role);
        
        UserEntity updated = userRepository.save(existingUser);
        return userMapper.toUserResponse(updated);
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
        userRepository.delete(user);
    }
}