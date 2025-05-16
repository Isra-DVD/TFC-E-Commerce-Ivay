package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.addressdto.AddressRequestDto;
import com.ivay.dtos.addressdto.AddressResponseDto;
import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.AddressService;
import com.ivay.service.UserEntityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:5173/")
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private UserEntityService userService;

	@Operation(
			summary     = "Fetch all addresses",
			description = "Retrieve a list of all addresses from the database",
			tags        = { "Address" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Addresses fetched successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { AddressResponseDto.class },
								example = """
										{
										  "timestamp": "2025-05-06T16:00:00.000Z",
										  "message": "Addresses fetched successfully",
										  "code": 200,
										  "data": [
										    { "id": 1, "userId": 5, "address": "123 Main St" },
										    { "id": 2, "userId": 7, "address": "456 Elm St" }
										  ]
										}
										"""
								)
						)
				)
	})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<List<AddressResponseDto>>> getAll() {
		List<AddressResponseDto> list = addressService.getAllAddresses();
		ApiResponseDto<List<AddressResponseDto>> response =
				new ApiResponseDto<>("Addresses fetched successfully", HttpStatus.OK.value(), list);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary     = "Fetch an address by ID",
			description = "Retrieve a single address from the database using its ID",
			tags        = { "Address" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Address fetched successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { AddressResponseDto.class },
								example = """
										{
										  "timestamp": "2025-05-06T16:05:00.000Z",
										  "message": "Address fetched successfully",
										  "code": 200,
										  "data": { "id": 1, "userId": 5, "address": "123 Main St" }
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "404",
				description  = "Address not found",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(implementation = ApiError.class)
						)
				)
	})
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<AddressResponseDto>> getById(
			@Parameter(description = "Address identifier", required = true)
			@PathVariable Long id
			) {
		AddressResponseDto AddressResponseDto = addressService.getAddressById(id);
		return ResponseEntity.ok(new ApiResponseDto<>("Address fetched successfully", 200, AddressResponseDto));
	}

	@Operation(
			summary     = "Fetch addresses by user ID",
			description = "Retrieve all addresses associated with a specific user ID",
			tags        = { "Address" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "200",
				description  = "Addresses fetched successfully",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(
								implementation = ApiResponseDto.class,
								subTypes      = { AddressResponseDto.class },
								example = """
										{
										  "timestamp": "2025-05-06T16:10:00.000Z",
										  "message": "Addresses fetched successfully",
										  "code": 200,
										  "data": [
										    { "id": 3, "userId": 5, "address": "789 Oak St" }
										  ]
										}
										"""
								)
						)
				),
		@ApiResponse(
				responseCode = "404",
				description  = "User not found",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(implementation = ApiError.class)
						)
				)
	})
	@GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<List<AddressResponseDto>>> getByUser(
			@Parameter(description = "User identifier", required = true)
			@PathVariable Long userId
			) {
		List<AddressResponseDto> list = addressService.getAddressesByUserId(userId);
		ApiResponseDto<List<AddressResponseDto>> response =
				new ApiResponseDto<>("Addresses fetched successfully", HttpStatus.OK.value(), list);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(
			summary = "Create a new address",
			description = "Add a new address to the database. Sólo el propio usuario o un ADMIN/SUPERADMIN puede crearlo.",
			tags = { "Address" }
			)
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Address created successfully",
				content = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema = @Schema(
								implementation = ApiResponseDto.class,
								subTypes = { AddressResponseDto.class }
								)
						)
				),
		@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
		schema = @Schema(implementation = ApiError.class))
				),
		@ApiResponse(responseCode = "403", description = "Forbidden: no permiso para crear esta dirección",
		content = @Content
				)
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<AddressResponseDto>> create(@Valid @RequestBody AddressRequestDto dto, Authentication auth) {

		AddressResponseDto created = addressService.createAddress(dto, auth.getName());

		ApiResponseDto<AddressResponseDto> resp = new ApiResponseDto<>("Address created successfully", HttpStatus.CREATED.value(), created);

		return new ResponseEntity<>(resp, HttpStatus.CREATED);
	}


	@Operation(
			summary = "Update an existing address",
			description = "Update the details of an existing address. Sólo el propio usuario o un ADMIN/SUPERADMIN puede modificarla.",
			tags = { "Address" }
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Address updated successfully",
				content = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema = @Schema(
								implementation = ApiResponseDto.class,
								subTypes = { AddressResponseDto.class }
								)
						)
				),
		@ApiResponse(responseCode = "400", description = "Invalid input",
		content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
		schema = @Schema(implementation = ApiError.class))
				),
		@ApiResponse(responseCode = "403", description = "Forbidden: no permiso para actualizar esta dirección",
		content = @Content
				),
		@ApiResponse(responseCode = "404", description = "Address not found",
		content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
		schema = @Schema(implementation = ApiError.class))
				)
	})
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<AddressResponseDto>> update(
			@Parameter(description = "Address identifier", required = true) @PathVariable Long id,
			@Valid @RequestBody AddressRequestDto dto,
			Authentication auth
			) {
		AddressResponseDto updated = 
				addressService.updateAddress(id, dto, auth.getName());
		ApiResponseDto<AddressResponseDto> resp = 
				new ApiResponseDto<>("Address updated successfully", HttpStatus.OK.value(), updated);
		return ResponseEntity.ok(resp);
	}

	@Operation(
			summary     = "Delete an address by ID",
			description = "Delete an address from the database using its ID",
			tags        = { "Address" }
			)
	@ApiResponses({
		@ApiResponse(
				responseCode = "204",
				description  = "Address deleted successfully",
				content      = @Content
				),
		@ApiResponse(
				responseCode = "404",
				description  = "Address not found",
				content      = @Content(
						mediaType = MediaType.APPLICATION_JSON_VALUE,
						schema    = @Schema(implementation = ApiError.class)
						)
				)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
			@Parameter(description = "Address identifier", required = true)
			@PathVariable Long id
			) {
		addressService.deleteAddress(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
