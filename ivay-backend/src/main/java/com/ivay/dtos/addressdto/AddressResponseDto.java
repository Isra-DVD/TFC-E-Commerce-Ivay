package com.ivay.dtos.addressdto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing an Address.
 *
 * Contains all fields returned by the API when fetching address information:
 * id, userId, address, zipCode, province, and locality.
 *
 * Validation and formatting of these fields is handled on the server side.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class AddressResponseDto {

    /**
     * Unique identifier of the address.
     */
    private Long id;

    /**
     * Identifier of the user who owns this address.
     */
    private Long userId;

    /**
     * Street address line.
     */
    private String address;

    /**
     * Postal code associated with the address.
     */
    private String zipCode;

    /**
     * Province or region of the address.
     */
    private String province;

    /**
     * Locality (city or town) of the address.
     */
    private String locality;
}
