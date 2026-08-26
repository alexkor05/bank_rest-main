package com.example.bankcards.outbox.dto;

public record UserRegisteredPayload(
        Long userId,
        String email,
        String firstName,
        String lastName
) {
}
