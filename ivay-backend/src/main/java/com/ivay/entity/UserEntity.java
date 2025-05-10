package com.ivay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"orders", "addresses", "cartItems"})
@EqualsAndHashCode(exclude = {"orders", "addresses", "cartItems"})
@Entity
@Table(name = "ecommerce2_users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 20)
    private String phone;

    private Boolean isEnabled;

    private Boolean accountNoExpired;

    private Boolean accountNoLocked;

    private Boolean credentialNoExpired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    @OneToMany(mappedBy = "user")
    private List<CartItem> cartItems;
}
