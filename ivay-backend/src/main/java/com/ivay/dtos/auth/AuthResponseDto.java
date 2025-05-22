package com.ivay.dtos.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for authentication requests.
 *
 * Provides the JWT access token and its type.
 *
 * Fields:
 * - accessToken: the JWT issued upon successful authentication
 * - tokenType: the token scheme prefix (defaults to "Bearer ")
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class AuthResponseDto {
    
    /**
     * The JWT access token returned by the authentication endpoint.
     */
    private String accessToken;

    /**
     * The token prefix indicating the authentication scheme.
     * Defaults to "Bearer ".
     */
    private String tokenType = "Bearer ";

    /**
     * Constructs a new AuthResponseDto with the given access token.
     *
     * @param accessToken the JWT to return to the client
     */
    public AuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
