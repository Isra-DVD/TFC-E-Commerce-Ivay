package com.ivay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivay.entity.Product;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Product entities.
 *
 * Extends JpaRepository to provide standard methods such as:
 * - save
 * - findById
 * - findAll
 * - deleteById
 *
 * Adds a custom finder to search products by partial name match, ignoring case.
 *
 * @since 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds all products whose name contains the given substring, case-insensitive.
     *
     * @param name substring to search for within product names
     * @return list of Product entities matching the search criterion
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}
