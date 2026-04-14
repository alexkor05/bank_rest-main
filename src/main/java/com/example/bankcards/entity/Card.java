package com.example.bankcards.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Card {
    private Long id;
    private String cardNumber;
    private BigDecimal balance;
    private LocalDate expiredDate;
}
