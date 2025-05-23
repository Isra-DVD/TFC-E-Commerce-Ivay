package com.ivay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.supplierdto.SupplierRequestDto;
import com.ivay.dtos.supplierdto.SupplierResponseDto;
import com.ivay.dtos.api.ApiError;
import com.ivay.dtos.api.ApiResponseDto;
import com.ivay.service.SupplierService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * REST controller for managing suppliers.
 *
 * Provides endpoints to create, read, update, and delete suppliers.
 *
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/api/suppliers", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = {
	    "http://localhost:3000",
	    "http://localhost:3001",
	    "http://localhost:5173",
	    "http://localhost:5174",
	    "http://localhost:5678"
	})@Tag(name = "Supplier", description = "Endpoints for managing suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * Retrieve all suppliers.
     *
     * @return HTTP 200 with list of {@link SupplierResponseDto}
     */
    @Operation(summary = "Fetch all suppliers", description = "Retrieve a list of all suppliers")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Suppliers fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { SupplierResponseDto.class }
                ),
                examples = @ExampleObject(value ="""
                    {
                      "timestamp":"2025-05-06T12:00:00Z",
                      "message":"Suppliers fetched successfully",
                      "code":200,
                      "data":[
                        {
                          "id":1,
                          "name":"TechGlobal Supplies",
                          "email":"sales@techglobal.com",
                          "address":"123 Innovation Drive, Tech City",
                          "phone":"555-0101",
                          "imageUrl":"http://example.com/logo.png"
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
    public ResponseEntity<ApiResponseDto<List<SupplierResponseDto>>> getAllSuppliers() {
        List<SupplierResponseDto> list = supplierService.getAllSuppliers();
        ApiResponseDto<List<SupplierResponseDto>> response =
            new ApiResponseDto<>("Suppliers fetched successfully", HttpStatus.OK.value(), list);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve a supplier by its ID.
     *
     * @param id supplier identifier
     * @return HTTP 200 with the {@link SupplierResponseDto}
     */
    @Operation(summary = "Fetch a supplier by ID", description = "Retrieve a supplier using its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier fetched successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { SupplierResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp":"2025-05-06T12:34:56.789012Z",
                      "message":"Supplier fetched successfully",
                      "code":200,
                      "data":{
                        "id":1,
                        "name":"TechGlobal Supplies",
                        "email":"sales@techglobal.com",
                        "address":"123 Innovation Drive, Tech City",
                        "phone":"555-0101",
                        "imageUrl":"http://example.com/logo.png"
                      }
                    }
                    """
            )
        )
        ),
        @ApiResponse(responseCode = "404", description = "Supplier not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<SupplierResponseDto>> getSupplierById(@PathVariable Long id) {
        SupplierResponseDto dto = supplierService.getSupplierById(id);
        ApiResponseDto<SupplierResponseDto> response =
            new ApiResponseDto<>("Supplier fetched successfully", HttpStatus.OK.value(), dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Create a new supplier.
     *
     * @param dto supplier payload
     * @return HTTP 201 with created {@link SupplierResponseDto}
     */
    @Operation(summary = "Create a new supplier", description = "Add a new supplier")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Supplier created successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { SupplierResponseDto.class }
                ),
                examples = @ExampleObject(value ="""
                    {
                      "timestamp":"2025-05-06T13:45:00.123456Z",
                      "message":"Supplier created successfully",
                      "code":201,
                      "data":{
                        "id":5,
                        "name":"GlobalParts Inc.",
                        "email":"contact@globalparts.com",
                        "address":"456 Industrial Rd",
                        "phone":"555-6789",
                        "imageUrl":"http://example.com/logo2.png"
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
    public ResponseEntity<ApiResponseDto<SupplierResponseDto>> createSupplier(
        @Valid @RequestBody SupplierRequestDto dto
    ) {
        SupplierResponseDto created = supplierService.createSupplier(dto);
        ApiResponseDto<SupplierResponseDto> response =
            new ApiResponseDto<>("Supplier created successfully", HttpStatus.CREATED.value(), created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update an existing supplier.
     *
     * @param id  supplier identifier
     * @param dto updated supplier payload
     * @return HTTP 200 with updated {@link SupplierResponseDto}
     */
    @Operation(summary = "Update an existing supplier", description = "Modify supplier using its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(
                    implementation = ApiResponseDto.class,
                    subTypes       = { SupplierResponseDto.class }
                ),
                examples = @ExampleObject(value = """
                    {
                      "timestamp":"2025-05-06T14:00:00.123456Z",
                      "message":"Supplier updated successfully",
                      "code":200,
                      "data":{
                        "id":3,
                        "name":"Updated Supplier Inc.",
                        "email":"updated@supplier.com",
                        "address":"789 New Road",
                        "phone":"555-0000",
                        "imageUrl":"http://example.com/logo3.png"
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
        @ApiResponse(responseCode = "404", description = "Supplier not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDto<SupplierResponseDto>> updateSupplier(
        @PathVariable Long id,
        @Valid @RequestBody SupplierRequestDto dto
    ) {
        SupplierResponseDto updated = supplierService.updateSupplier(id, dto);
        ApiResponseDto<SupplierResponseDto> response =
            new ApiResponseDto<>("Supplier updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a supplier by its ID.
     *
     * @param id supplier identifier
     * @return HTTP 204 No Content
     */
    @Operation(summary = "Delete a supplier by ID", description = "Remove a supplier using its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Supplier not found",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema    = @Schema(implementation = ApiError.class)
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
