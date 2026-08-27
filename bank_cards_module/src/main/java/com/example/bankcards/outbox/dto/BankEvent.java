package com.example.bankcards.outbox.dto;

import com.example.bankcards.dto.EventType;
import com.example.bankcards.outbox.entity.AggregateType;
import com.example.bankcards.outbox.entity.EventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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
