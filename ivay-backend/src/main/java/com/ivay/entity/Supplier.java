package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * JPA entity representing a Supplier.
 *
 * Maps to the "suppliers" table and contains:
 * - id: unique identifier of the supplier
 * - name: name of the supplier
 * - email: contact email address
 * - address: physical address
 * - phone: contact phone number
 * - imageUrl: URL of the supplier's image or logo
 * - products: list of products provided by this supplier
 *
 * @since 1.0.0
 */
@Data
@ToString(exclude = "products")
@EqualsAndHashCode(exclude = "products")
@Entity
@Table(name = "suppliers")
public class Supplier {

    /**
     * Primary key, auto-generated identifier of the supplier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the supplier.
     * Maximum length is 100 characters.
     */
    @Column(length = 100)
    private String name;

    /**
     * Email address of the supplier.
     * Maximum length is 255 characters.
     */
    @Column(length = 255)
    private String email;

    /**
     * Physical address of the supplier.
     * Maximum length is 255 characters.
     */
    @Column(length = 255)
    private String address;

    /**
     * Phone number of the supplier.
     * Maximum length is 20 characters.
     */
    @Column(length = 20)
    private String phone;
    
    /**
     * URL pointing to the supplier's image or logo.
     * Maximum length is 255 characters.
     */
    @Column(length = 255)
    private String imageUrl;

    /**
     * Products associated with this supplier.
     * One supplier can provide many products.
     */
    @OneToMany(mappedBy = "supplier")
    private List<Product> products;
}
