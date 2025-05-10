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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "http://localhost:5173/")
public class SupplierController {

	@Autowired
	private SupplierService supplierService;


	@Operation(
			summary     = "Fetch all suppliers",
			description = "Retrieve a list of all Suppliers stored in the system",
			tags        = { "Supplier" }
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description  = "Suppliers fetched successfully",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(
									implementation = ApiResponseDto.class,
									subTypes      = { SupplierResponseDto.class },
									example = "{ … }"
									)
							)
					),
			@ApiResponse(
					responseCode = "401",
					description  = "Unauthorized — missing or invalid token",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = ApiError.class)
							)
					),
			@ApiResponse(
					responseCode = "403",
					description  = "Forbidden — insufficient privileges",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = ApiError.class)
							)
					),
			@ApiResponse(
					responseCode = "500",
					description  = "Internal server error",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = ApiError.class)
							)
					)
	})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<List<SupplierResponseDto>>> getAllSuppliers() {
		List<SupplierResponseDto> list = supplierService.getAllSuppliers();
		ApiResponseDto<List<SupplierResponseDto>> response = new ApiResponseDto<>("Suppliers fetched successfully", HttpStatus.OK.value(), list);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Operation(
			summary     = "Fetch a supplier by ID",
			description = "Retrieve a supplier from the database using its ID",
			tags        = { "Supplier" }
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description  = "Supplier fetched successfully",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(
									implementation = ApiResponseDto.class,
									subTypes      = { SupplierResponseDto.class },
									example = """
											{
											  "timestamp": "2025-05-06T12:34:56.789012",
											  "message": "Supplier fetched successfully",
											  "code": 200,
											  "data": {
											    "id": 1,
											    "name": "TechGlobal Supplies",
											    "email": "sales@techglobal.com",
											    "address": "123 Innovation Drive, Tech City",
											    "phone": "555-0101"
											  }
											}
											"""
									)
							)
					),
			@ApiResponse(
					responseCode = "404",
					description  = "Supplier not found",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = ApiError.class)
							)
					)
	})
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<SupplierResponseDto>> getSupplierById(@PathVariable Long id) {
		SupplierResponseDto dto = supplierService.getSupplierById(id);
		ApiResponseDto<SupplierResponseDto> response = new ApiResponseDto<>("Supplier fetched successfully", HttpStatus.OK.value(), dto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Operation(
			summary     = "Create a new supplier",
			description = "Add a new supplier to the database",
			tags        = { "Supplier" }
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "201",
					description  = "Supplier created successfully",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(
									implementation = ApiResponseDto.class,
									subTypes      = { SupplierResponseDto.class },
									example = """
											{
											  "timestamp": "2025-05-06T13:45:00.123456",
											  "message": "Supplier created successfully",
											  "code": 201,
											  "data": {
											    "id": 5,
											    "name": "GlobalParts Inc.",
											    "email": "contact@globalparts.com",
											    "address": "456 Industrial Rd",
											    "phone": "555-6789"
											  }
											}
											"""
									)
							)
					),
			@ApiResponse(
					responseCode = "400",
					description  = "Invalid input",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = ApiError.class)
							)
					)
	})
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<SupplierResponseDto>> createSupplier(@Valid @RequestBody SupplierRequestDto dto) {
		SupplierResponseDto created = supplierService.createSupplier(dto);
		ApiResponseDto<SupplierResponseDto> response = new ApiResponseDto<>("Supplier created successfully", HttpStatus.CREATED.value(), created);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}


	@Operation(
			summary     = "Update an existing supplier",
			description = "Update the details of an existing supplier in the database using the supplier ID",
			tags        = { "Supplier" }
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description  = "Supplier updated successfully",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(
									implementation = ApiResponseDto.class,
									subTypes      = { SupplierResponseDto.class },
									example = """
											{
											  "timestamp": "2025-05-06T14:00:00.123456",
											  "message": "Supplier updated successfully",
											  "code": 200,
											  "data": {
											    "id": 3,
											    "name": "Updated Supplier Inc.",
											    "email": "updated@supplier.com",
											    "address": "789 New Road",
											    "phone": "555-0000"
											  }
											}
											"""
									)
							)
					),
			@ApiResponse(
					responseCode = "400",
					description  = "Invalid input",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = ApiError.class)
							)
					),
			@ApiResponse(
					responseCode = "404",
					description  = "Supplier not found",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = ApiError.class)
							)
					)
	})
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponseDto<SupplierResponseDto>> updateSupplier(@PathVariable Long id, @Valid @RequestBody SupplierRequestDto dto) {
		SupplierResponseDto updated = supplierService.updateSupplier(id, dto);
		ApiResponseDto<SupplierResponseDto> response = new ApiResponseDto<>("Supplier updated successfully", HttpStatus.OK.value(), updated);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Operation(
			summary     = "Delete a supplier by ID",
			description = "Delete a supplier from the database using the supplier ID",
			tags        = { "Supplier" }
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "204",
					description  = "Supplier deleted successfully",
					content      = @Content
					),
			@ApiResponse(
					responseCode = "404",
					description  = "Supplier not found",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = ApiError.class)
							)
					)
	})
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
		supplierService.deleteSupplier(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
