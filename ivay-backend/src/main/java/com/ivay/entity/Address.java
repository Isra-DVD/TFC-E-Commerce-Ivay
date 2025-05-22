package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * JPA entity representing an Address.
 *
 * Maps to the "addresses" table and contains:
 * - id: primary key
 * - user: the owning UserEntity
 * - address: street address line
 * - zipCode: postal code
 * - province: province or region
 * - locality: city or town
 *
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "addresses")
public class Address {

    /**
     * Primary key, auto-generated identifier of the address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the owning user.
     * Many addresses can belong to one user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * Street address line.
     * Maximum length is 255 characters.
     */
    @Column(length = 255)
    private String address;
    
    /**
     * Postal code for the address.
     * Maximum length is 7 characters.
     */
    @Column(length = 7)
    private String zipCode;
    
    /**
     * Province or region of the address.
     * Maximum length is 50 characters.
     */
    @Column(length = 50)
    private String province;
    
    /**
     * Locality (city or town) of the address.
     * Maximum length is 50 characters.
     */
    @Column(length = 50)
    private String locality;
}
