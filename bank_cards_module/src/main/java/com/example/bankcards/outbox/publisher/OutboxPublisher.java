package com.example.bankcards.outbox.publisher;

import com.example.bankcards.outbox.dto.BankEvent;
import com.example.bankcards.outbox.entity.EventStatus;
import com.example.bankcards.outbox.entity.OutboxEvent;
import com.example.bankcards.outbox.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OutboxEventRepository eventRepository;
    private final ObjectMapper objectMapper;

    @Value("${outbox.batchSize}")
    private int batchSize;

    @Value("${app.kafka.topics.bankEvents}")
    private String bankEventsTopic;

    @Value("${outbox.maxRetryAttempts}")
    private int maxRetryAttempts;


    @Transactional
    @Scheduled(cron = "*/10 * * * * *")
    public void publish() {
        List<OutboxEvent> eventList = eventRepository.findNewEventsForPublishing(batchSize);
        for (OutboxEvent event : eventList) {
            publishEvent(event);
        }

    }

    private void publishEvent(OutboxEvent event) {
        try {
            JsonNode jsonPayload = objectMapper.readTree(event.getPayload());

            BankEvent bankEvent = new BankEvent(
                    event.getEventId(),
                    event.getAggregateType(),
                    event.getAggregateId(),
                    event.getEventType(),
                    jsonPayload,
                    event.getCreatedAt()
            );

            kafkaTemplate.send(bankEventsTopic, bankEvent).get();
            event.setEventStatus(EventStatus.PUBLISHED);
            event.setPublishedAt(LocalDateTime.now());
        } catch (Exception e) {
            handlePublishingError(event, e);



        }

        eventRepository.save(event);
    }

    private void handlePublishingError(OutboxEvent event, Exception e) {
        event.setRetryCount(event.getRetryCount() + 1);
        event.setLastError(e.getMessage());

        if(event.getRetryCount() >= maxRetryAttempts) {
            event.setEventStatus(EventStatus.FAILED);
        }
    }

}
