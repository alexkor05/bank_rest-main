package com.example.bankcards.dto;

public record NotificationEvent(
        String email,
        String firstname,
        String lastname,
        EventType eventType,
        String message
) {
}
