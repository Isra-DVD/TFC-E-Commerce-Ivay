package com.ivay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivay.entity.Order;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on Order entities.
 *
 * Extends JpaRepository to provide standard methods such as:
 * - save
 * - findById
 * - findAll
 * - deleteById
 *
 * Adds a custom finder for retrieving orders by user.
 *
 * @since 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId the identifier of the user
     * @return a list of Order entities associated with the user
     */
    List<Order> findByUser_Id(Long userId);
}
