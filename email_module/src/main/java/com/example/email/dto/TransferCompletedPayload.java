package com.example.email.dto;

import java.math.BigDecimal;

public record TransferCompletedPayload(
        Long userId,
        Long fromCardId,
        String fromMaskedCardNumber,
        Long toCardId,
        String toMaskedCardNumber,
        String email,
        BigDecimal amount
) implements EventPayload {
}
