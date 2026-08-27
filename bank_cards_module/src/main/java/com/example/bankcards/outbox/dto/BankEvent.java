package com.example.bankcards.outbox.dto;

import com.example.bankcards.dto.EventType;
import com.example.bankcards.outbox.entity.AggregateType;
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
