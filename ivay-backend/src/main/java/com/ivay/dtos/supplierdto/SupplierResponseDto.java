package com.ivay.dtos.supplierdto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing a Supplier.
 *
 * Contains all fields returned by the API when fetching supplier information:
 * id, name, email, address, phone, and imageUrl.
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
public class SupplierResponseDto {

    /**
     * Unique identifier of the supplier.
     */
    private Long id;

    /**
     * Name of the supplier.
     */
    private String name;

    /**
     * Email address of the supplier.
     */
    private String email;

    /**
     * Physical address of the supplier.
     */
    private String address;

    /**
     * Phone number of the supplier.
     */
    private String phone;

    /**
     * URL of the supplier's image.
     */
    private String imageUrl;
}
