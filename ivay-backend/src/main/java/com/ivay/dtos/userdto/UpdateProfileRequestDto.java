package com.ivay.dtos.userdto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating a user's profile information.
 *
 * Contains validated fields for the account name, full name,
 * email address, phone number, and user address.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class UpdateProfileRequestDto {

    /**
     * Account name for the user.
     * Must not be blank and must not exceed 100 characters.
     */
    @NotBlank(message = "El nombre de la cuenta no puede estar vacio")
    @Size(max = 100, message = "El nombre de la cuenta no puede exceder 100 caracteres")
    private String name;

    /**
     * Full name of the user.
     * Must not be blank and must not exceed 50 characters.
     */
    @NotBlank(message = "El nombre completo del usuario no puede estar vacio")
    @Size(max = 50, message = "El nombre completo del usuario no puede exceder 50 caracteres")
    private String fullName;

    /**
     * Email address of the user.
     * Must not be blank, must be a valid email format,
     * and must not exceed 50 characters.
     */
    @NotBlank(message = "El email del usuario no puede estar vacio")
    @Email(message = "El email debe ser válido")
    @Size(max = 50, message = "El email del usuario no puede exceder 50 caracteres")
    private String email;

    /**
     * Phone number of the user.
     * Must not be blank and must not exceed 20 characters.
     */
    @NotBlank(message = "El teléfono del usuario no puede estar vacio")
    @Size(max = 20, message = "El teléfono del usuario no puede exceder 20 caracteres")
    private String phone;

    /**
     * Physical address of the user.
     * Must not be blank and has a maximum column length of 255.
     */
    @NotBlank(message = "La dirección del usuario es obligatoria")
    @Column(length = 255)
    private String userAddress;
}
