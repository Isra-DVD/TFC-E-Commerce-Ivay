package com.ivay.dtos.addressdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating or updating an Address.
 *
 * Contains all required fields to submit a new address:
 * userId, address, zipCode, province, and locality.
 *
 * Validation constraints ensure mandatory fields are present
 * and within the expected size limits.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class AddressRequestDto {
	
	/**
     * Identifier of the user to whom this address belongs.
     * Must not be null.
     */
	@NotNull(message = "El ID de usuario es obligatorio")
    private Long userId;
	
	/**
     * Street address line.
     * Must not be blank and no longer than 255 characters.
     */
	@NotBlank(message = "La dirección es obligatoria")
	@Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
    private String address;
	
	/**
     * Postal code for the address.
     * Must not be blank and no longer than 7 characters.
     */
	@NotBlank(message = "El código postal es obligatorio")
    @Size(max = 7, message = "El código postal no puede exceder 7 caracteres")
    private String zipCode;
	
	/**
     * Province or region of the address.
     * Must not be blank and no longer than 50 characters.
     */
	@NotBlank(message = "La provincia es obligatoria")
	@Size(max = 50, message = "La provincia no puede exceder 50 caracteres")
    private String province;
	
	/**
     * Locality (city or town) of the address.
     * Must not be blank and no longer than 50 characters.
     */
	@NotBlank(message = "La localidad es obligatoria")
	@Size(max = 50, message = "La localidad no puede exceder 50 caracteres")
    private String locality;
}
