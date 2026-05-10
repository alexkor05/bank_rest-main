package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", unique = true, nullable = true)
    private String cardNumber;

    @Column(name = "balance", nullable = true)
    private BigDecimal balance;

    @Column(name = "expired_date", nullable = true)
    private LocalDate expiredDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
