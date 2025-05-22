package com.ivay.dtos.userdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a User.
 *
 * Contains the following fields with validation constraints:
 * - name: username, required and max 100 characters
 * - fullName: user's full name, required and max 50 characters
 * - email: user's email, required, must be valid format, max 100 characters
 * - password: user's password, required
 * - phone: user's phone number, required and max 20 characters
 * - userAddress: user's physical address, required, max 255 characters
 * - isEnabled: whether the user account is enabled
 * - accountNoExpired: whether the account has not expired
 * - accountNoLocked: whether the account is not locked
 * - credentialNoExpired: whether the credentials have not expired
 * - roleId: identifier of the role assigned to the user, required
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class UserRequestDto {

    /**
     * Username of the user.
     * Must not be blank and must not exceed 100 characters.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;
    
    /**
     * Full name of the user.
     * Must not be blank and must not exceed 50 characters.
     */
    @NotBlank(message = "El nombre completo del usuario es obligatorio")
    @Size(max = 50, message = "El nombre completo no puede exceder 50 caracteres")
    private String fullName;

    /**
     * Email address of the user.
     * Must not be blank, must be valid email format, and must not exceed 100 characters.
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String email;

    /**
     * Password for the user account.
     * Must not be blank.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /**
     * Phone number of the user.
     * Must not be blank and must not exceed 20 characters.
     */
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String phone;
    
    /**
     * Physical address of the user.
     * Must not be blank and must not exceed 255 characters.
     */
    @NotBlank(message = "La dirección del usuario es obligatoria")
    @Column(length = 255)
    private String userAddress;

    /**
     * Whether the user account is enabled.
     */
    private Boolean isEnabled;

    /**
     * Whether the user account has not expired.
     */
    private Boolean accountNoExpired;

    /**
     * Whether the user account is not locked.
     */
    private Boolean accountNoLocked;

    /**
     * Whether the user credentials have not expired.
     */
    private Boolean credentialNoExpired;

    /**
     * Identifier of the role assigned to the user.
     * Must not be null.
     */
    @NotNull(message = "El ID de rol es obligatorio")
    private Long roleId;
}
