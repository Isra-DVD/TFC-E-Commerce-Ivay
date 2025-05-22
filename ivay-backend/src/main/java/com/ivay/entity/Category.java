package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * JPA entity representing a product category.
 *
 * Maps to the "categories" table and contains:
 * - id: primary key identifier
 * - name: name of the category
 * - products: list of products belonging to this category
 *
 * @since 1.0.0
 */
@Data
@ToString(exclude = "products")
@EqualsAndHashCode(exclude = "products")
@Entity
@Table(name = "categories")
public class Category {

    /**
     * Primary key, auto-generated identifier of the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the category.
     * Maximum length is 50 characters.
     */
    @Column(length = 50)
    private String name;

    /**
     * Products assigned to this category.
     * One category can have many products.
     */
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
