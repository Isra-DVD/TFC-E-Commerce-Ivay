package com.ivay.service;

import java.util.List;

import com.ivay.dtos.userdto.ChangePasswordRequestDto;
import com.ivay.dtos.userdto.UpdateProfileRequestDto;
import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;

/**
 * Service interface for managing user accounts.
 *
 * Provides methods to:
 * - retrieve all users or individual users by ID or username
 * - create, update, and delete user accounts
 * - update profile information for the authenticated user
 * - change the authenticated user's password
 *
 * @since 1.0.0
 */
public interface UserEntityService {

    /**
     * Retrieves all user accounts in the system.
     *
     * @return a list of {@link UserResponseDto} representing all users
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Retrieves a single user account by its identifier.
     *
     * @param id the identifier of the user to retrieve
     * @return the {@link UserResponseDto} for the given user
     */
    UserResponseDto getUserById(Long id);

    /**
     * Retrieves a single user account by username.
     *
     * @param username the username of the user to retrieve
     * @return the {@link UserResponseDto} for the given username
     */
    UserResponseDto getByUsername(String username);

    /**
     * Creates a new user account.
     *
     * @param userRequestDto the data for the new user
     * @return the created {@link UserResponseDto}
     */
    UserResponseDto createUser(UserRequestDto userRequestDto);

    /**
     * Updates an existing user account.
     *
     * @param id the identifier of the user to update
     * @param userRequestDto the new data for the user
     * @return the updated {@link UserResponseDto}
     */
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

    /**
     * Deletes a user account by its identifier.
     *
     * @param id the identifier of the user to delete
     */
    void deleteUser(Long id);

    /**
     * Updates profile information for the authenticated user.
     *
     * @param username the username of the user whose profile is updated
     * @param dto the {@link UpdateProfileRequestDto} containing profile changes
     * @return the updated {@link UserResponseDto}
     */
    UserResponseDto updateProfile(String username, UpdateProfileRequestDto dto);

    /**
     * Changes the password for the authenticated user.
     *
     * @param username the username of the user whose password is changed
     * @param dto the {@link ChangePasswordRequestDto} containing current and new passwords
     */
    void changePassword(String username, ChangePasswordRequestDto dto);
}
