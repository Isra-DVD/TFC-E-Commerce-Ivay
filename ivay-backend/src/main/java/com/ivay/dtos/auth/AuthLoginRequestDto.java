package com.ivay.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for user login authentication.
 *
 * Contains credentials required to perform authentication:
 * - username: the identifier of the user (must not be blank)
 * - password: the secret credential of the user (must not be blank)
 *
 * Validation constraints ensure that neither field is empty or null.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class AuthLoginRequestDto {

    /**
     * The username of the user attempting to authenticate.
     * Must not be blank.
     */
    @NotBlank
    private String username;

    /**
     * The password of the user attempting to authenticate.
     * Must not be blank.
     */
    @NotBlank
    private String password;
}
