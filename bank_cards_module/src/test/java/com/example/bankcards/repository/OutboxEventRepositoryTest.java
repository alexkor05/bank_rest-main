package com.example.bankcards.repository;

import com.example.bankcards.IntegrationTestBase;
import com.example.bankcards.dto.EventType;
import com.example.bankcards.outbox.entity.AggregateType;
import com.example.bankcards.outbox.entity.EventStatus;
import com.example.bankcards.outbox.entity.OutboxEvent;
import com.example.bankcards.outbox.repository.OutboxEventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.hibernate.annotations.processing.Exclude;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.AutoConfigureTestEntityManager;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;



public class OutboxEventRepositoryTest extends IntegrationTestBase {

    @Autowired
    private OutboxEventRepository outboxEventRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSaveAndReadOutboxEvent() {
        OutboxEvent event = new OutboxEvent();
        UUID eventId = UUID.randomUUID();
        event.setEventId(eventId);
        event.setAggregateType(AggregateType.USER);
        event.setAggregateId(1L);
        event.setEventType(EventType.USER_REGISTERED);
        event.setPayload("""
                {
                    "userId": 1,
                    "email": "alex@gmail.com",
                    "firstname": "Alex",
                    "lastname": "Kor"
                }
                """);
        event.setEventStatus(EventStatus.NEW);
        event.setCreatedAt(LocalDateTime.now());

        OutboxEvent saved = outboxEventRepository.saveAndFlush(event);
        entityManager.clear();

        // then
        OutboxEvent found = outboxEventRepository
                .findById(saved.getId())
                .orElseThrow();

        assertThat(found.getEventId()).isEqualTo(eventId);
        assertThat(found.getPayload()).contains("alex@gmail.com");
        assertThat(found.getEventStatus()).isEqualTo(EventStatus.NEW);
        assertThat(found.getEventType()).isEqualTo(EventType.USER_REGISTERED);
        assertThat(found.getAggregateType()).isEqualTo(AggregateType.USER);
        assertThat(found.getAggregateId()).isEqualTo(1L);


    }
}
