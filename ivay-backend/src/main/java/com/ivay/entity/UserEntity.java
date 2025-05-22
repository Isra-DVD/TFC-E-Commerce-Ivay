package com.ivay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * JPA entity representing a user account in the system.
 *
 * Maps to the "users" table and contains:
 * - id: primary key identifier
 * - name: unique username
 * - fullName: user's full display name
 * - email: contact email address
 * - password: hashed user password
 * - phone: contact phone number
 * - userAddress: physical address of the user
 * - isEnabled: whether the account is active
 * - accountNoExpired: whether the account has not expired
 * - accountNoLocked: whether the account is not locked
 * - credentialNoExpired: whether the credentials have not expired
 * - role: the security role assigned to the user
 * - orders: list of orders placed by the user
 * - addresses: list of addresses associated with the user
 * - cartItems: list of cart items owned by the user
 *
 * Cascade and fetch strategies are configured on relations as needed.
 *
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"orders", "addresses", "cartItems"})
@EqualsAndHashCode(exclude = {"orders", "addresses", "cartItems"})
@Entity
@Table(name = "users")
public class UserEntity {

    /**
     * Primary key, auto-generated identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique username of the account.
     * Maximum length is 100 characters.
     */
    @Column(length = 100, unique = true)
    private String name;

    /**
     * Full name of the user for display purposes.
     * Maximum length is 50 characters.
     */
    @Column(length = 50)
    private String fullName;
    
    /**
     * Email address of the user.
     * Maximum length is 100 characters.
     */
    @Column(length = 100)
    private String email;

    /**
     * Hashed password of the user.
     * Maximum length is 255 characters.
     */
    @Column(length = 255)
    private String password;

    /**
     * Contact phone number of the user.
     * Maximum length is 20 characters.
     */
    @Column(length = 20)
    private String phone;
    
    /**
     * Physical address of the user.
     * Maximum length is 255 characters.
     */
    @Column(length = 255)
    private String userAddress;

    /**
     * Indicates if the user account is enabled.
     */
    private Boolean isEnabled;

    /**
     * Indicates if the user account has not expired.
     */
    private Boolean accountNoExpired;

    /**
     * Indicates if the user account is not locked.
     */
    private Boolean accountNoLocked;

    /**
     * Indicates if the user's credentials have not expired.
     */
    private Boolean credentialNoExpired;

    /**
     * Security role assigned to this user.
     * Many users can share the same role.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    /**
     * Orders placed by this user.
     * One user can have many orders.
     */
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    /**
     * Addresses associated with this user.
     * One user can have many addresses.
     */
    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    /**
     * Cart items belonging to this user.
     * One user can have many cart items.
     */
    @OneToMany(mappedBy = "user")
    private List<CartItem> cartItems;
}
