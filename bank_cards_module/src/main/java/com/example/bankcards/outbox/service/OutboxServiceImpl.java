package com.example.bankcards.outbox.service;

import com.example.bankcards.dto.EventType;

import com.example.bankcards.outbox.entity.AggregateType;
import com.example.bankcards.outbox.entity.EventStatus;
import com.example.bankcards.outbox.entity.OutboxEvent;
import com.example.bankcards.outbox.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService{

    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxRepository;

    @Transactional
    @Override
    public void saveEvent(AggregateType aggregateType, Long aggregateId, EventType eventType, Object payload) {

            String json = objectMapper.writeValueAsString(payload);
            OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setAggregateId(aggregateId);
            outboxEvent.setEventId(UUID.randomUUID());
            outboxEvent.setAggregateType(aggregateType);
            outboxEvent.setEventType(eventType);
            outboxEvent.setEventStatus(EventStatus.NEW);
            outboxEvent.setPayload(json);
            outboxEvent.setCreatedAt(LocalDateTime.now());
            outboxRepository.save(outboxEvent);

    }

}
