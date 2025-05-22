package com.ivay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivay.entity.OrderItem;

import java.util.List;

/**
 * Repository interface for performing CRUD operations on OrderItem entities.
 *
 * Extends JpaRepository to provide standard data access methods such as save,
 * findById, findAll, and deleteById.
 *
 * Adds custom query methods for retrieving order items by order or product.
 *
 * @since 1.0.0
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Retrieves all order items associated with a specific order.
     *
     * @param orderId the identifier of the order
     * @return a list of OrderItem entities for the given order
     */
    List<OrderItem> findByOrder_Id(Long orderId);

    /**
     * Retrieves all order items that include a specific product.
     *
     * @param productId the identifier of the product
     * @return a list of OrderItem entities containing the given product
     */
    List<OrderItem> findByProduct_Id(Long productId);
}
