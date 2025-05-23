package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.userdto.ChangePasswordRequestDto;
import com.ivay.dtos.userdto.UpdateProfileRequestDto;
import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;
import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.UserEntityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * REST controller for managing users.
 *
 * Provides endpoints to create, read, update, and delete users,
 * as well as to fetch and modify the authenticated user's own profile
 * and to change the authenticated user's password.
 *
 * Responses use {@link ApiResponseDto} for successful operations
 * and {@link ApiError} for errors.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "http://localhost:3001",
	    "http://localhost:5173",
	    "http://localhost:5174",
	    "http://localhost:5678"
	})@Tag(name = "User", description = "Endpoints for managing users")
public class UserController {

    @Autowired
    private UserEntityService userService;

    /**
     * Retrieve all users.
     *
     * @return HTTP 200 with list of {@link UserResponseDto}
     */
    @Operation(summary = "Fetch all users", description = "Retrieve a list of all users")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { UserResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:00:00.000Z",
                      "message": "Users fetched successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 1,
                          "name": "Alice",
                          "email": "alice@example.com",
                          "phone": "123456789",
                          "userAddress": "123 Main St",
                          "isEnabled": true,
                          "accountNoExpired": true,
                          "accountNoLocked": true,
                          "credentialNoExpired": true,
                          "roleId": 2
                        }
                      ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        List<UserResponseDto> list = userService.getAllUsers();
        ApiResponseDto<List<UserResponseDto>> response =
            new ApiResponseDto<>("Users fetched successfully", HttpStatus.OK.value(), list);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a user by their ID.
     *
     * @param id the user ID
     * @return HTTP 200 with the {@link UserResponseDto}
     */
    @Operation(summary = "Fetch a user by ID", description = "Retrieve a user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { UserResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:05:00.000Z",
                      "message": "User fetched successfully",
                      "code": 200,
                      "data": {
                        "id": 1,
                        "name": "Alice",
                        "email": "alice@example.com",
                        "phone": "123456789",
                        "userAddress": "123 Main St",
                        "isEnabled": true,
                        "accountNoExpired": true,
                        "accountNoLocked": true,
                        "credentialNoExpired": true,
                        "roleId": 2
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(
        @Parameter(description = "User identifier", required = true)
        @PathVariable Long id
    ) {
        UserResponseDto dto = userService.getUserById(id);
        ApiResponseDto<UserResponseDto> response =
            new ApiResponseDto<>("User fetched successfully", HttpStatus.OK.value(), dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new user.
     *
     * @param dto the user data
     * @return HTTP 201 with the created {@link UserResponseDto}
     */
    @Operation(summary = "Create a new user", description = "Add a new user to the database")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { UserResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:10:00.000Z",
                      "message": "User created successfully",
                      "code": 201,
                      "data": {
                        "id": 2,
                        "name": "Bob",
                        "email": "bob@example.com",
                        "phone": "987654321",
                        "userAddress": "456 Elm St",
                        "isEnabled": true,
                        "accountNoExpired": true,
                        "accountNoLocked": true,
                        "credentialNoExpired": true,
                        "roleId": 3
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> createUser(
        @Valid @RequestBody UserRequestDto dto
    ) {
        UserResponseDto created = userService.createUser(dto);
        ApiResponseDto<UserResponseDto> response =
            new ApiResponseDto<>("User created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing user by ID.
     *
     * @param id  the user ID
     * @param dto the updated user data
     * @return HTTP 200 with the updated {@link UserResponseDto}
     */
    @Operation(summary = "Update an existing user", description = "Update a user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { UserResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:15:00.000Z",
                      "message": "User updated successfully",
                      "code": 200,
                      "data": {
                        "id": 1,
                        "name": "Alice Updated",
                        "email": "alice.new@example.com",
                        "phone": "123000789",
                        "userAddress": "789 Oak St",
                        "isEnabled": true,
                        "accountNoExpired": true,
                        "accountNoLocked": false,
                        "credentialNoExpired": true,
                        "roleId": 2
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUser(
        @Parameter(description = "User identifier", required = true)
        @PathVariable Long id,
        @Valid @RequestBody UserRequestDto dto
    ) {
        UserResponseDto updated = userService.updateUser(id, dto);
        ApiResponseDto<UserResponseDto> response =
            new ApiResponseDto<>("User updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a user by ID.
     *
     * @param id the user ID
     * @return HTTP 204 No Content
     */
    @Operation(summary = "Delete a user by ID", description = "Delete a user by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "User identifier", required = true)
        @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieve profile of the authenticated user.
     *
     * @param authToken the authentication token
     * @return HTTP 200 with {@link UserResponseDto}
     */
    @Operation(
        summary      = "Fetch my profile",
        description  = "Retrieve the authenticated user's profile",
        security     = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { UserResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-07T10:00:00.000Z",
                      "message": "Profile fetched successfully",
                      "code": 200,
                      "data": {
                        "id": 1,
                        "name": "Alice",
                        "email": "alice@example.com",
                        "phone": "123456789",
                        "userAddress": "123 Main St",
                        "isEnabled": true,
                        "accountNoExpired": true,
                        "accountNoLocked": true,
                        "credentialNoExpired": true,
                        "roleId": 4
                      }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getMyProfile(Authentication authToken) {
        String username = authToken.getName();
        UserResponseDto profile = userService.getByUsername(username);
        ApiResponseDto<UserResponseDto> response =
            new ApiResponseDto<>("Profile fetched successfully", HttpStatus.OK.value(), profile);
        return ResponseEntity.ok(response);
    }

    /**
     * Update profile of the authenticated user.
     *
     * @param authToken the authentication token
     * @param dto       the new profile data
     * @return HTTP 200 with updated {@link UserResponseDto}
     */
    @Operation(summary = "Update my profile", description = "Modify the authenticated user's profile")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { UserResponseDto.class }
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/me/profile")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateProfile(
        Authentication authToken,
        @Valid @RequestBody UpdateProfileRequestDto dto
    ) {
        String username = authToken.getName();
        UserResponseDto updated = userService.updateProfile(username, dto);
        ApiResponseDto<UserResponseDto> response =
            new ApiResponseDto<>("Profile updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Change password of the authenticated user.
     *
     * @param authToken the authentication token
     * @param dto       contains current and new passwords
     * @return HTTP 204 No Content
     */
    @Operation(
        summary      = "Change own password",
        description  = "Allows the authenticated user to change their password",
        security     = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden: current password incorrect",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PatchMapping(value = "/me/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changePassword(
        Authentication authToken,
        @Valid @RequestBody ChangePasswordRequestDto dto
    ) {
        String username = authToken.getName();
        userService.changePassword(username, dto);
        return ResponseEntity.noContent().build();
    }
}
