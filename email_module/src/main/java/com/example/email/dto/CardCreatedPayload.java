package com.example.email.dto;

public record CardCreatedPayload (
        Long cardId,
        Long userId,
        String email,
        String maskedCardNumber

) implements EventPayload{
}
