package com.ivay.dtos.userdto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing a User.
 *
 * Contains all fields returned by the API when fetching user information:
 * id, name, fullName, email, phone, userAddress, isEnabled,
 * accountNoExpired, accountNoLocked, credentialNoExpired, and roleId.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class UserResponseDto {

    /**
     * Unique identifier of the user.
     */
    private Long id;

    /**
     * Username of the user.
     */
    private String name;

    /**
     * Full name of the user.
     */
    private String fullName;

    /**
     * Email address of the user.
     */
    private String email;

    /**
     * Phone number of the user.
     */
    private String phone;

    /**
     * Physical address of the user.
     */
    private String userAddress;

    /**
     * Indicates if the user account is enabled.
     */
    private Boolean isEnabled;

    /**
     * Indicates if the user account has not expired.
     */
    private Boolean accountNoExpired;

    /**
     * Indicates if the user account is not locked.
     */
    private Boolean accountNoLocked;

    /**
     * Indicates if the user credentials have not expired.
     */
    private Boolean credentialNoExpired;

    /**
     * Identifier of the role assigned to the user.
     */
    private Long roleId;
}
