package com.ivay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * JPA entity representing a security role.
 *
 * Maps to the "roles" table and contains:
 * - id: primary key identifier
 * - roleName: name of the role
 * - users: list of users assigned this role
 *
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Entity
@Table(name = "roles")
public class Role {

    /**
     * Primary key, auto-generated identifier of the role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the role.
     * Must not be blank and maximum length is 50 characters.
     */
    @Column(length = 50)
    @NotBlank(message = "Role name must not be blank")
    private String roleName;

    /**
     * Users associated with this role.
     * One role can be assigned to many users.
     */
    @OneToMany(mappedBy = "role")
    private List<UserEntity> users;
}
