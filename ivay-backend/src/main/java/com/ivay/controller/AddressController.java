package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.addressdto.AddressRequestDto;
import com.ivay.dtos.addressdto.AddressResponseDto;
import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * REST controller for managing addresses.
 *
 * Exposes endpoints to create, retrieve, update, and delete addresses.
 * All responses are wrapped in an {@link ApiResponseDto} or return an error
 * represented by {@link ApiError}.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "http://localhost:3001",
	    "http://localhost:5173",
	    "http://localhost:5174",
	    "http://localhost:5678"
	})
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * Retrieve all addresses.
     *
     * @return HTTP 200 with list of {@link AddressResponseDto} in ApiResponseDto
     */
    @Operation(
        summary     = "Fetch all addresses",
        description = "Retrieve a list of all addresses in the system",
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
                    subTypes      = { AddressResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:00:00.000Z",
                      "message": "Addresses fetched successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 1,
                          "userId": 5,
                          "address": "123 Main St",
                          "zipCode": "12345",
                          "province": "State",
                          "locality": "City"
                        },
                        {
                          "id": 2,
                          "userId": 7,
                          "address": "456 Elm St",
                          "zipCode": "67890",
                          "province": "Province",
                          "locality": "Town"
                        }
                      ]
                    }
                    """)
            )
        )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<List<AddressResponseDto>>> getAll() {
        List<AddressResponseDto> list = addressService.getAllAddresses();
        ApiResponseDto<List<AddressResponseDto>> response =
            new ApiResponseDto<>("Addresses fetched successfully", HttpStatus.OK.value(), list);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve an address by its ID.
     *
     * @param id the ID of the address
     * @return HTTP 200 with the {@link AddressResponseDto} in ApiResponseDto
     * @throws ResourceNotFoundException if address not found
     */
    @Operation(
        summary     = "Fetch an address by ID",
        description = "Retrieve a single address by its identifier",
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
                    subTypes      = { AddressResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:05:00.000Z",
                      "message": "Address fetched successfully",
                      "code": 200,
                      "data": {
                        "id": 1,
                        "userId": 5,
                        "address": "123 Main St",
                        "zipCode": "12345",
                        "province": "State",
                        "locality": "City"
                      }
                    }
                    """)
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
        AddressResponseDto dto = addressService.getAddressById(id);
        ApiResponseDto<AddressResponseDto> response =
            new ApiResponseDto<>("Address fetched successfully", HttpStatus.OK.value(), dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all addresses for a given user.
     *
     * @param userId the ID of the user
     * @return HTTP 200 with list of {@link AddressResponseDto} in ApiResponseDto
     * @throws ResourceNotFoundException if user not found
     */
    @Operation(
        summary     = "Fetch addresses by user ID",
        description = "Retrieve all addresses associated with a specific user",
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
                    subTypes      = { AddressResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T16:10:00.000Z",
                      "message": "Addresses fetched successfully",
                      "code": 200,
                      "data": [
                        {
                          "id": 3,
                          "userId": 5,
                          "address": "789 Oak St",
                          "zipCode": "54321",
                          "province": "Region",
                          "locality": "Village"
                        }
                      ]
                    }
                    """)
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
    @PreAuthorize(
        "hasAnyRole('ADMIN','SUPERADMIN') or @userEntityServiceImpl.getByUsername(authentication.name).id == #userId"
    )
    public ResponseEntity<ApiResponseDto<List<AddressResponseDto>>> getByUser(
        @Parameter(description = "User identifier", required = true)
        @PathVariable Long userId
    ) {
        List<AddressResponseDto> list = addressService.getAddressesByUserId(userId);
        ApiResponseDto<List<AddressResponseDto>> response =
            new ApiResponseDto<>("Addresses fetched successfully", HttpStatus.OK.value(), list);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new address.
     *
     * Only the user themself or an ADMIN/SUPERADMIN may create an address.
     *
     * @param dto  the address data
     * @param auth the current authentication
     * @return HTTP 201 with created {@link AddressResponseDto} in ApiResponseDto
     * @throws AccessDeniedException if unauthorized
     * @throws MethodArgumentNotValidException if validation fails
     */
    @Operation(
        summary     = "Create a new address",
        description = "Add a new address for a user. Only that user or an ADMIN/SUPERADMIN may perform this operation.",
        tags        = { "Address" }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Address created successfully",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema       = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { AddressResponseDto.class }
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Invalid request body",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description  = "Forbidden: not allowed to create address for this user",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize(
        "hasAnyRole('ADMIN','SUPERADMIN') or #dto.userId == @userEntityServiceImpl.getByUsername(authentication.name).id"
    )
    public ResponseEntity<ApiResponseDto<AddressResponseDto>> create(
        @Valid @RequestBody AddressRequestDto dto,
        Authentication auth
    ) {
        AddressResponseDto created = addressService.createAddress(dto, auth.getName());
        ApiResponseDto<AddressResponseDto> response =
            new ApiResponseDto<>("Address created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing address.
     *
     * Only the owner or an ADMIN/SUPERADMIN may update an address.
     *
     * @param id   the ID of the address to update
     * @param dto  the new address data
     * @param auth the current authentication
     * @return HTTP 200 with updated {@link AddressResponseDto} in ApiResponseDto
     * @throws AccessDeniedException if unauthorized
     * @throws ResourceNotFoundException if address not found
     */
    @Operation(
        summary     = "Update an existing address",
        description = "Modify the details of an existing address. Only the owner or an ADMIN/SUPERADMIN may perform this operation.",
        tags        = { "Address" }
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Address updated successfully",
            content      = @Content(
                mediaType       = MediaType.APPLICATION_JSON_VALUE,
                schema           = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { AddressResponseDto.class }
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Invalid request body",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description  = "Forbidden: not allowed to update this address",
            content      = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
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
    @PutMapping(
        value    = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize(
        "hasAnyRole('ADMIN','SUPERADMIN') or @addressServiceImpl.isOwner(#id, authentication.name)"
    )
    public ResponseEntity<ApiResponseDto<AddressResponseDto>> update(
        @Parameter(description = "Address identifier", required = true)
        @PathVariable Long id,
        @Valid @RequestBody AddressRequestDto dto,
        Authentication auth
    ) {
        AddressResponseDto updated = addressService.updateAddress(id, dto, auth.getName());
        ApiResponseDto<AddressResponseDto> response =
            new ApiResponseDto<>("Address updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an address by its ID.
     *
     * Only the owner or an ADMIN/SUPERADMIN may delete an address.
     *
     * @param id the ID of the address to delete
     * @return HTTP 204 on successful deletion
     * @throws ResourceNotFoundException if address not found
     */
    @Operation(
        summary     = "Delete an address by ID",
        description = "Remove an address from the system by its identifier",
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
    @PreAuthorize(
        "hasAnyRole('ADMIN','SUPERADMIN') or @addressServiceImpl.isOwner(#id, authentication.name)"
    )
    public ResponseEntity<Void> delete(
        @Parameter(description = "Address identifier", required = true)
        @PathVariable Long id
    ) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
