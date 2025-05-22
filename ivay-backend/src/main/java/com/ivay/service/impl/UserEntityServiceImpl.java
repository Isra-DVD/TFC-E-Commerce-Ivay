package com.ivay.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ivay.dtos.userdto.ChangePasswordRequestDto;
import com.ivay.dtos.userdto.UpdateProfileRequestDto;
import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;
import com.ivay.entity.Role;
import com.ivay.entity.UserEntity;
import com.ivay.exception.ResourceNotFoundException;
import com.ivay.mappers.UserMapper;
import com.ivay.repository.RoleRepository;
import com.ivay.repository.UserRepository;
import com.ivay.service.UserEntityService;

/**
 * Service implementation for managing user accounts.
 *
 * Implements methods to create, read, update and delete users,
 * as well as updating profile information and changing passwords.
 *
 * @since 1.0.0
 */
@Service
public class UserEntityServiceImpl implements UserEntityService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieves all users in the system.
     *
     * @return list of UserResponseDto representing all users
     */
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(userMapper::toUserResponse)
                             .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by its identifier.
     *
     * @param id identifier of the user to retrieve
     * @return UserResponseDto for the specified user
     * @throws ResourceNotFoundException if no user is found
     */
    @Override
    public UserResponseDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
        return userMapper.toUserResponse(user);
    }

    /**
     * Creates a new user account.
     *
     * Ensures the username is unique and the specified role exists.
     * Encodes the provided password before saving.
     *
     * @param userRequestDto data for the new user
     * @return UserResponseDto of the created user
     * @throws IllegalArgumentException if the username is already in use
     * @throws ResourceNotFoundException if the specified role is not found
     */
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.findUserEntityByName(userRequestDto.getName()).isPresent()) {
            throw new IllegalArgumentException("Ese nombre de cuenta ya está en uso");
        }

        Role role = roleRepository.findById(userRequestDto.getRoleId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Role with id: " + userRequestDto.getRoleId() + " not found"));

        UserEntity user = userMapper.toUser(userRequestDto);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        UserEntity saved = userRepository.save(user);
        return userMapper.toUserResponse(saved);
    }

    /**
     * Updates an existing user account.
     *
     * Updates all fields and optionally re-encodes the password if provided.
     *
     * @param id identifier of the user to update
     * @param userRequestDto new data for the user
     * @return UserResponseDto of the updated user
     * @throws ResourceNotFoundException if user or role is not found
     */
    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        UserEntity existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));

        Role role = roleRepository.findById(userRequestDto.getRoleId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Role with id: " + userRequestDto.getRoleId() + " not found"));

        existingUser.setName(userRequestDto.getName());
        existingUser.setFullName(userRequestDto.getFullName());
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setPhone(userRequestDto.getPhone());
        existingUser.setUserAddress(userRequestDto.getUserAddress());
        existingUser.setIsEnabled(userRequestDto.getIsEnabled());
        existingUser.setAccountNoExpired(userRequestDto.getAccountNoExpired());
        existingUser.setAccountNoLocked(userRequestDto.getAccountNoLocked());
        existingUser.setCredentialNoExpired(userRequestDto.getCredentialNoExpired());
        existingUser.setRole(role);

        String newRawPassword = userRequestDto.getPassword();
        if (newRawPassword != null && !newRawPassword.isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(newRawPassword));
        }

        UserEntity updated = userRepository.save(existingUser);
        return userMapper.toUserResponse(updated);
    }

    /**
     * Deletes a user account by its identifier.
     *
     * @param id identifier of the user to delete
     * @throws ResourceNotFoundException if no user is found
     */
    @Override
    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));
        userRepository.delete(user);
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username to look up
     * @return UserResponseDto for the specified username
     * @throws ResourceNotFoundException if no user is found
     */
    @Override
    public UserResponseDto getByUsername(String username) {
        UserEntity currentUser = userRepository.findUserEntityByName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return userMapper.toUserResponse(currentUser);
    }

    /**
     * Updates profile information for the authenticated user.
     *
     * Only updates name, fullName, email, phone, and address.
     *
     * @param username the username whose profile to update
     * @param dto profile data to apply
     * @return UserResponseDto of the updated profile
     * @throws ResourceNotFoundException if no user is found
     */
    @Override
    public UserResponseDto updateProfile(String username, UpdateProfileRequestDto dto) {
        UserEntity existingUser = userRepository.findUserEntityByName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        existingUser.setName(dto.getName());
        existingUser.setFullName(dto.getFullName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPhone(dto.getPhone());
        existingUser.setUserAddress(dto.getUserAddress());

        UserEntity savedUser = userRepository.save(existingUser);
        return userMapper.toUserResponse(savedUser);
    }

    /**
     * Changes the authenticated user's password.
     *
     * Verifies the current password before encoding and saving the new one.
     *
     * @param username the username whose password to change
     * @param dto contains current and new passwords
     * @throws ResourceNotFoundException if no user is found
     * @throws BadCredentialsException if the current password does not match
     */
    @Override
    public void changePassword(String username, ChangePasswordRequestDto dto) {
        UserEntity user = userRepository.findUserEntityByName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
