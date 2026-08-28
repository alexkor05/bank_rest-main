package com.example.bankcards.outbox.dto;

public record CardBlockedPayload (
        Long cardId,
        Long userId,
        String email,
        String maskedCardNumber
) implements EventPayload {
}
