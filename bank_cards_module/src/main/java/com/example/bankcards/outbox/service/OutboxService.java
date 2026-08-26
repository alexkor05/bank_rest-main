package com.example.bankcards.outbox.service;

import com.example.bankcards.dto.EventType;
import com.example.bankcards.outbox.dto.EventPayload;
import com.example.bankcards.outbox.entity.AggregateType;

public interface OutboxService {

    void saveEvent(AggregateType aggregateType, Long aggregateId,  EventType eventType, EventPayload payload);
}
