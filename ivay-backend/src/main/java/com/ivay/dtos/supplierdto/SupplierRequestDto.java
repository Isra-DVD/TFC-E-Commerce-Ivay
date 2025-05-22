package com.ivay.dtos.supplierdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating a Supplier.
 *
 * Contains the following fields with validation constraints:
 * - name: supplier name, required and max 100 characters
 * - email: supplier email, required, must be valid format, max 255 characters
 * - address: supplier address, required and max 255 characters
 * - phone: supplier phone number, required and max 20 characters
 * - imageUrl: URL of the supplier's image, required and max 255 characters
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class SupplierRequestDto {

    /**
     * Name of the supplier.
     * Must not be blank and must not exceed 100 characters.
     */
    @NotBlank(message = "El nombre del supplier es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    /**
     * Email address of the supplier.
     * Must not be blank, must be a valid email format, and must not exceed 255 characters.
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    private String email;

    /**
     * Physical address of the supplier.
     * Must not be blank and must not exceed 255 characters.
     */
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String address;

    /**
     * Phone number of the supplier.
     * Must not be blank and must not exceed 20 characters.
     */
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String phone;

    /**
     * URL of the supplier's image.
     * Must not be blank and must not exceed 255 characters.
     */
    @NotBlank(message = "La url de la imagen del proveedor es obligatoria")
    @Size(max = 255, message = "La url no puede exceder 255 caracteres")
    private String imageUrl;
}
