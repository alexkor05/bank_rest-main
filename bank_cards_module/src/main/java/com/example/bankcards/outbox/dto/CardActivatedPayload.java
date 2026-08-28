package com.example.bankcards.outbox.dto;

public record CardActivatedPayload (
        Long cardId,
        Long userId,
        String email,
        String maskedCardNumber
) implements EventPayload{
}
