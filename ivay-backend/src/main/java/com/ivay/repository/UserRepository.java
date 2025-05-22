package com.ivay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ivay.entity.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserEntity.
 *
 * Extends JpaRepository to provide standard methods such as:
 * - save
 * - findById
 * - findAll
 * - deleteById
 *
 * Adds custom query methods for retrieving users by email, name, and role.
 *
 * @since 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Finds a user by email, ignoring case.
     *
     * @param email the email address to search for
     * @return an Optional containing the matching UserEntity if found
     */
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    /**
     * Finds all users whose name contains the given substring, case-insensitive.
     *
     * @param name substring to search for within user names
     * @return list of UserEntity matching the search criterion
     */
    List<UserEntity> findByNameContainingIgnoreCase(String name);

    /**
     * Finds all users assigned to a given role.
     *
     * @param roleId the identifier of the role
     * @return list of UserEntity having the specified role
     */
    List<UserEntity> findByRole_Id(Long roleId);

    /**
     * Finds a user by exact username.
     *
     * @param name the username to search for
     * @return an Optional containing the matching UserEntity if found
     */
    Optional<UserEntity> findUserEntityByName(String name);
}
