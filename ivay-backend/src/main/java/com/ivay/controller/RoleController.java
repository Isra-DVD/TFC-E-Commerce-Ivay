package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.roledto.RoleRequestDto;
import com.ivay.dtos.roledto.RoleResponseDto;
import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.Valid;

/**
 * REST controller for managing application roles.
 *
 * Provides endpoints to create, read, update, and delete roles.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:5174" })
@Tag(name = "Role", description = "Endpoints for managing roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * Retrieve all roles.
     *
     * @return HTTP 200 with a list of {@link RoleResponseDto}
     */
    @Operation(summary = "Fetch all roles", description = "Retrieve a list of all roles from the database")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Roles fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { RoleResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T15:00:00.123456",
                      "message": "Roles fetched successfully",
                      "code": 200,
                      "data": [
                        { "id": 1, "roleName": "ADMIN" },
                        { "id": 2, "roleName": "USER" }
                      ]
                    }
                    """
                )
            )
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<RoleResponseDto>>> getAllRoles() {
        List<RoleResponseDto> roles = roleService.getAllRoles();
        ApiResponseDto<List<RoleResponseDto>> response =
            new ApiResponseDto<>("Roles fetched successfully", HttpStatus.OK.value(), roles);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a role by its ID.
     *
     * @param id the role identifier
     * @return HTTP 200 with the {@link RoleResponseDto}
     */
    @Operation(summary = "Fetch a role by ID", description = "Retrieve a role using its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { RoleResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T15:05:00.654321",
                      "message": "Role fetched successfully",
                      "code": 200,
                      "data": { "id": 1, "roleName": "ADMIN" }
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<RoleResponseDto>> getRoleById(
        @Parameter(description = "Role identifier", required = true)
        @PathVariable Long id
    ) {
        RoleResponseDto role = roleService.getRoleById(id);
        ApiResponseDto<RoleResponseDto> response =
            new ApiResponseDto<>("Role fetched successfully", HttpStatus.OK.value(), role);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new role.
     *
     * @param roleRequestDto the role data
     * @return HTTP 201 with the created {@link RoleResponseDto}
     */
    @Operation(summary = "Create a new role", description = "Add a new role to the database")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Role created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { RoleResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T15:10:00.987654",
                      "message": "Role created successfully",
                      "code": 201,
                      "data": { "id": 3, "roleName": "MANAGER" }
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
    public ResponseEntity<ApiResponseDto<RoleResponseDto>> createRole(
        @Valid @RequestBody RoleRequestDto roleRequestDto
    ) {
        RoleResponseDto created = roleService.createRole(roleRequestDto);
        ApiResponseDto<RoleResponseDto> response =
            new ApiResponseDto<>("Role created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing role.
     *
     * @param id             the role identifier
     * @param roleRequestDto the new role data
     * @return HTTP 200 with the updated {@link RoleResponseDto}
     */
    @Operation(summary = "Update an existing role", description = "Modify a role using its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Role updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { RoleResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp": "2025-05-06T15:30:00.000Z",
                      "message": "Role updated successfully",
                      "code": 200,
                      "data": {
                        "id": 3,
                        "roleName": "MANAGER"
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
        @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<RoleResponseDto>> updateRole(
        @Parameter(description = "Role identifier", required = true) @PathVariable Long id,
        @Parameter(description = "Updated role object", required = true, schema = @Schema(implementation = RoleRequestDto.class))
        @Valid @RequestBody RoleRequestDto roleRequestDto
    ) {
        RoleResponseDto updated = roleService.updateRole(id, roleRequestDto);
        ApiResponseDto<RoleResponseDto> response =
            new ApiResponseDto<>("Role updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a role by its ID.
     *
     * @param id the role identifier
     * @return HTTP 204 No Content
     */
    @Operation(summary = "Delete a role by ID", description = "Remove a role using its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(
        @Parameter(description = "Role identifier", required = true) @PathVariable Long id
    ) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
