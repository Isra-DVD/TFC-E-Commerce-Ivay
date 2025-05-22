package com.ivay.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivay.entity.Supplier;

/**
 * Repository interface for performing CRUD operations on Supplier entities.
 *
 * Extends JpaRepository to provide standard data access methods such as:
 * - save
 * - findById
 * - findAll
 * - deleteById
 *
 * Adds a custom finder to search suppliers by partial name match, ignoring case.
 *
 * @since 1.0.0
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    /**
     * Finds all suppliers whose name contains the given substring, case-insensitive.
     *
     * @param name substring to search for within supplier names
     * @return list of Supplier entities matching the search criterion
     */
    List<Supplier> findByNameContainingIgnoreCase(String name);
}
