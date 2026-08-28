package com.example.email.dto;

public record CardActivatedPayload (
        Long cardId,
        Long userId,
        String email,
        String maskedCardNumber
) implements EventPayload{
}
