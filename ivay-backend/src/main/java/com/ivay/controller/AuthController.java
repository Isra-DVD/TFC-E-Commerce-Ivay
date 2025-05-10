package com.ivay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ivay.dtos.auth.AuthLoginRequestDto;
import com.ivay.dtos.auth.AuthResponseDto;
import com.ivay.dtos.api.ApiError;
import com.ivay.service.impl.UserDetailsServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173/")
public class AuthController {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Operation(
			summary     = "Authenticate user and obtain JWT",
			description = "Validates the provided username and password, and returns a JWT access token upon successful authentication.",
			tags        = { "Auth" }
			)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description  = "Authentication successful",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = AuthResponseDto.class),
							examples  = @ExampleObject(
									name  = "SuccessfulAuth",
									value = """
											{
											  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
											  "tokenType": "Bearer "
											}
											"""
									)
							)
					),
			@ApiResponse(
					responseCode = "400",
					description  = "Invalid request payload",
					content      = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema    = @Schema(implementation = ApiError.class)
							)
					),
			@ApiResponse(
					responseCode = "401",
					description  = "Invalid credentials",
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
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthResponseDto> login(
			@RequestBody(
					description = "Credentials for logging in",
					required    = true,
					content     = @Content(schema = @Schema(implementation = AuthLoginRequestDto.class))
					)
			@Valid @org.springframework.web.bind.annotation.RequestBody AuthLoginRequestDto loginRequest
			) {
		AuthResponseDto authResponse = userDetailsService.login(loginRequest);
		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}
}