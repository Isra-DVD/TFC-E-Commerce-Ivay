package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(length = 255)
    private String address;
    
    @Column(length = 7)
    private String zipCode;
    
    @Column(length = 50)
    private String province;
    
    @Column(length = 50)
    private String locality;
}