package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString(exclude = "products")
@EqualsAndHashCode(exclude = "products")
@Entity
@Table(name = "ecommerce2_suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String phone;

     @OneToMany(mappedBy = "supplier")
    private List<Product> products;
}
