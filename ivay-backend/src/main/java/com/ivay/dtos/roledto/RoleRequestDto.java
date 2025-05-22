package com.ivay.dtos.roledto;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a Role.
 *
 * Contains the roleName field with validation constraints
 * to ensure it is provided and does not exceed the length limit.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class RoleRequestDto {

    /**
     * Name of the role.
     * Must not be blank and must not exceed 50 characters.
     */
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre del rol no puede exceder 50 caracteres")
    private String roleName;
}
