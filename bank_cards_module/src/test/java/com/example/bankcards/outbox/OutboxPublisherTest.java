package com.example.bankcards.outbox;

import com.example.bankcards.dto.EventType;
import com.example.bankcards.outbox.dto.BankEvent;
import com.example.bankcards.outbox.entity.AggregateType;
import com.example.bankcards.outbox.entity.EventStatus;
import com.example.bankcards.outbox.entity.OutboxEvent;
import com.example.bankcards.outbox.publisher.OutboxPublisher;
import com.example.bankcards.outbox.repository.OutboxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OutboxPublisherTest {
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private OutboxEventRepository eventRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OutboxPublisher outboxPublisher;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                outboxPublisher,
                "batchSize",
                100
        );

        ReflectionTestUtils.setField(
                outboxPublisher,
                "bankEventsTopic",
                "bank-events"
        );
    }

    @Test
    void shouldPublishEventAndMarkAsPublished() throws Exception {

        // given
        OutboxEvent event = createNewEvent();

        JsonNode payload = mock(JsonNode.class);

        when(eventRepository.findNewEventsForPublishing(100))
                .thenReturn(List.of(event));

        when(objectMapper.readTree(event.getPayload()))
                .thenReturn(payload);

        CompletableFuture<SendResult<String, Object>> future =
                CompletableFuture.completedFuture(null);

        when(kafkaTemplate.send(
                eq("bank-events"),
                any(BankEvent.class)
        )).thenReturn(future);


        // when
        outboxPublisher.publish();


        // then
        assertThat(event.getEventStatus())
                .isEqualTo(EventStatus.PUBLISHED);

        assertThat(event.getPublishedAt())
                .isNotNull();

        assertThat(event.getRetryCount())
                .isZero();

        assertThat(event.getLastError())
                .isNull();

        verify(eventRepository)
                .findNewEventsForPublishing(100);

        verify(kafkaTemplate)
                .send(
                        eq("bank-events"),
                        any(BankEvent.class)
                );
    }

    @Test
    void shouldIncreaseRetryCountWhenKafkaPublishingFails() throws Exception {

        // given
        OutboxEvent event = createNewEvent();

        JsonNode payload = mock(JsonNode.class);

        when(eventRepository.findNewEventsForPublishing(100))
                .thenReturn(List.of(event));

        when(objectMapper.readTree(event.getPayload()))
                .thenReturn(payload);

        CompletableFuture<SendResult<String, Object>> failedFuture =
                new CompletableFuture<>();

        failedFuture.completeExceptionally(
                new RuntimeException("Kafka is unavailable")
        );

        when(kafkaTemplate.send(
                eq("bank-events"),
                any(BankEvent.class)
        )).thenReturn(failedFuture);


        // when
        outboxPublisher.publish();


        // then
        assertThat(event.getEventStatus())
                .isEqualTo(EventStatus.FAILED);

        assertThat(event.getPublishedAt())
                .isNull();

        assertThat(event.getRetryCount())
                .isEqualTo(1);

        assertThat(event.getLastError())
                .contains("Kafka is unavailable");

        verify(kafkaTemplate)
                .send(
                        eq("bank-events"),
                        any(BankEvent.class)
                );
    }

    @Test
    void shouldMarkEventAsFailedWhenMaxRetryAttemptsReached() throws Exception {

        // given
        OutboxEvent event = createNewEvent();
        event.setRetryCount(2);

        JsonNode payload = mock(JsonNode.class);

        when(eventRepository.findNewEventsForPublishing(100))
                .thenReturn(List.of(event));

        when(objectMapper.readTree(event.getPayload()))
                .thenReturn(payload);

        CompletableFuture<SendResult<String, Object>> failedFuture =
                new CompletableFuture<>();

        failedFuture.completeExceptionally(
                new RuntimeException("Kafka is unavailable")
        );

        when(kafkaTemplate.send(
                eq("bank-events"),
                any(BankEvent.class)
        )).thenReturn(failedFuture);

        // when
        outboxPublisher.publish();

        // then
        assertThat(event.getRetryCount())
                .isEqualTo(3);

        assertThat(event.getEventStatus())
                .isEqualTo(EventStatus.FAILED);
    }

    private OutboxEvent createNewEvent() {

        OutboxEvent event = new OutboxEvent();

        event.setEventId(UUID.randomUUID());
        event.setAggregateType(AggregateType.USER);
        event.setAggregateId(1L);
        event.setEventType(EventType.USER_REGISTERED);

        event.setPayload("""
                {
                    "userId": 1,
                    "email": "alex@gmail.com",
                    "firstName": "Alex",
                    "lastName": "Kor"
                }
                """);

        event.setEventStatus(EventStatus.NEW);
        event.setRetryCount(0);
        event.setCreatedAt(LocalDateTime.now());

        return event;
    }
}
