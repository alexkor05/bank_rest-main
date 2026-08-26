package com.example.bankcards.outbox.dto;

public record CardCreatedPayload (
        Long cardId,
        Long userId,
        String maskedCardNumber

){
}
