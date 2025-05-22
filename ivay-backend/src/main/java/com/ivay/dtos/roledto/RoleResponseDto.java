package com.ivay.dtos.roledto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing a Role.
 *
 * Contains the identifier and name of the role as returned by the API.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class RoleResponseDto {

    /**
     * Unique identifier of the role.
     */
    private Long id;

    /**
     * Name of the role.
     */
    private String roleName;
}
