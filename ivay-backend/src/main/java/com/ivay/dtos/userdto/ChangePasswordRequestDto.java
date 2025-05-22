package com.ivay.dtos.userdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for changing a user's password.
 *
 * Contains the current password for verification and
 * the new password to set.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class ChangePasswordRequestDto {

    /**
     * The user's current password.
     * Must not be blank.
     */
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    /**
     * The user's new desired password.
     * Must not be blank.
     */
    @NotBlank(message = "New password is required")
    private String newPassword;
}
