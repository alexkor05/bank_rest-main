package com.example.email.dto;



public record NotificationEvent(
        String email,
        String firstname,
        String lastname,
        com.example.email.dto.EventType eventType,
        String message
) {
}
