package com.ivay.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString(exclude = "orderItems")
@EqualsAndHashCode(exclude = "orderItems")
@Entity
@Table(name = "ecommerce2_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private LocalDateTime billDate;

    @Column(length = 50)
    private String paymentMethod;

    @Column(precision = 3, scale = 2)
    private BigDecimal globalDiscount;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmountDiscounted;

    private Integer tax;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
}