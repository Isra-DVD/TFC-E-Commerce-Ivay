package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.userdto.UserRequestDto;
import com.ivay.dtos.userdto.UserResponseDto;
import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.UserEntityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:5173/")
@Tag(name = "User", description = "Endpoints para gestión de usuarios")
public class UserController {

	@Autowired
	private UserEntityService userService;

	@Operation(
			summary = "Fetch all users",
			description = "Retrieve a list of all users from the database"
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Users fetched successfully",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(
									implementation = ApiResponseDto.class,
									subTypes = { UserResponseDto.class },
									example = """
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
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class)
							)
					)
	})
	@GetMapping
	public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
		List<UserResponseDto> list = userService.getAllUsers();
		ApiResponseDto<List<UserResponseDto>> response = new ApiResponseDto<>("Users fetched successfully", HttpStatus.OK.value(), list);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary = "Fetch a user by ID",
			description = "Retrieve a user from the database using the user ID"
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "User fetched successfully",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(
									implementation = ApiResponseDto.class,
									subTypes = { UserResponseDto.class },
									example = """
											{
											  "timestamp": "2025-05-06T16:05:00.000Z",
											  "message": "User fetched successfully",
											  "code": 200,
											  "data": {
											    "id": 1,
											    "name": "Alice",
											    "email": "alice@example.com",
											    "phone": "123456789",
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
			@ApiResponse(
					responseCode = "404",
					description = "User not found",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class)
							)
					)
	})
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(@PathVariable Long id) {
		UserResponseDto dto = userService.getUserById(id);
		ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User fetched successfully", HttpStatus.OK.value(), dto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary = "Create a new user",
			description = "Add a new user to the database"
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description = "User created successfully",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(
									implementation = ApiResponseDto.class,
									subTypes = { UserResponseDto.class },
									example = """
											{
											  "timestamp": "2025-05-06T16:10:00.000Z",
											  "message": "User created successfully",
											  "code": 201,
											  "data": {
											    "id": 2,
											    "name": "Bob",
											    "email": "bob@example.com",
											    "phone": "987654321",
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
			@ApiResponse(
					responseCode = "400",
					description = "Invalid input",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class)
							)
					)
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<UserResponseDto>> createUser(@Valid @RequestBody UserRequestDto dto) {
		UserResponseDto created = userService.createUser(dto);
		ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User created successfully", HttpStatus.CREATED.value(), created);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(
			summary = "Update an existing user",
			description = "Update the details of an existing user in the database using the user ID"
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "User updated successfully",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(
									implementation = ApiResponseDto.class,
									subTypes = { UserResponseDto.class },
									example = """
											{
											  "timestamp": "2025-05-06T16:15:00.000Z",
											  "message": "User updated successfully",
											  "code": 200,
											  "data": {
											    "id": 1,
											    "name": "Alice Updated",
											    "email": "alice.new@example.com",
											    "phone": "123000789",
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
			@ApiResponse(
					responseCode = "400",
					description = "Invalid input",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class)
							)
					),
			@ApiResponse(
					responseCode = "404",
					description = "User not found",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class)
							)
					)
	})
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<UserResponseDto>> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto dto) {
		UserResponseDto updated = userService.updateUser(id, dto);
		ApiResponseDto<UserResponseDto> response = new ApiResponseDto<>("User updated successfully", HttpStatus.OK.value(), updated);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Operation(
			summary = "Delete a user by ID",
			description = "Delete a user from the database using the user ID"
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "204",
					description = "User deleted successfully",
					content = @Content
					),
			@ApiResponse(
					responseCode = "404",
					description = "User not found",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ApiError.class)
							)
					)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
