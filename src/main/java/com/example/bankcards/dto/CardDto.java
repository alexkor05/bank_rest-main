package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardDto(
        Long id,
        String cardNumber,
        LocalDate expireDate,
        BigDecimal balance,
        Status status,
        Long userId
) {}
