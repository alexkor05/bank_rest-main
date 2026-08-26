package com.example.bankcards.outbox.dto;

import java.math.BigDecimal;

public record TransferCompletedPayload(
        Long fromCardId,
        Long toCardId,
        BigDecimal amount
) {
}
