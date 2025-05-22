package com.ivay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivay.entity.Address;

/**
 * Repository interface for performing CRUD operations on Address entities.
 *
 * Extends JpaRepository to provide standard methods such as:
 * - save
 * - findById
 * - findAll
 * - deleteById
 *
 * Spring will automatically implement this interface at runtime.
 *
 * @since 1.0.0
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
