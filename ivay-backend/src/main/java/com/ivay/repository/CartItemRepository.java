package com.ivay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivay.entity.CartItem;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on CartItem entities.
 *
 * Extends JpaRepository to provide standard methods such as:
 * - save
 * - findById
 * - findAll
 * - deleteById
 *
 * Adds custom finder methods for user-specific operations:
 * - findByUser_Id
 * - findByUser_IdAndProduct_Id
 * - deleteByUser_Id
 *
 * @since 1.0.0
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Retrieves all cart items belonging to a specific user.
     *
     * @param userId the identifier of the user
     * @return a list of CartItem entities for the user
     */
    List<CartItem> findByUser_Id(Long userId);

    /**
     * Retrieves a cart item for a specific user and product combination.
     *
     * @param userId    the identifier of the user
     * @param productId the identifier of the product
     * @return an Optional containing the matching CartItem if found
     */
    Optional<CartItem> findByUser_IdAndProduct_Id(Long userId, Long productId);

    /**
     * Deletes all cart items associated with a specific user.
     *
     * @param userId the identifier of the user whose cart items will be deleted
     */
    void deleteByUser_Id(Long userId);
}
