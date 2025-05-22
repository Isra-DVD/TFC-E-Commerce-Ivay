package com.ivay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivay.entity.Category;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Category entities.
 *
 * Extends JpaRepository to provide standard methods such as:
 * - save
 * - findById
 * - findAll
 * - deleteById
 *
 * Adds a custom finder to search by partial name match, ignoring case.
 *
 * @since 1.0.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds all categories whose name contains the given substring, case-insensitive.
     *
     * @param name substring to search for within category names
     * @return list of Category entities matching the search criterion
     */
    List<Category> findByNameContainingIgnoreCase(String name);
}
