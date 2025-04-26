package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ecommerce2_addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 255)
    private String address;
}