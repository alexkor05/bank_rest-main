package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCardRequest(
        @NotBlank String cardNumber,
        @DateTimeFormat(pattern = "yyyy-MM-dd") @Future LocalDate expiredDate,
        @NotNull BigDecimal balance,
        @NotNull Status status,
        @NotNull Long userId
) {
}
