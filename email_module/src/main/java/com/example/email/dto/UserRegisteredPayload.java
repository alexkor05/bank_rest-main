package com.example.email.dto;

public record UserRegisteredPayload(
        Long userId,
        String email,
        String firstName,
        String lastName
) implements EventPayload {
}
