package com.example.email.dto;


import tools.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;

public record BankEvent(
        UUID eventId,
        AggregateType aggregateType,
        Long aggregateId,
        EventType eventType,
        JsonNode payload,
        LocalDateTime createdAt

) {
}
