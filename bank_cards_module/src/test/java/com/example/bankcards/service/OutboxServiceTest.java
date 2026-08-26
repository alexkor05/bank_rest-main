package com.example.bankcards.service;

import com.example.bankcards.dto.EventType;
import com.example.bankcards.outbox.dto.UserRegisteredPayload;
import com.example.bankcards.outbox.entity.AggregateType;
import com.example.bankcards.outbox.entity.EventStatus;
import com.example.bankcards.outbox.entity.OutboxEvent;
import com.example.bankcards.outbox.repository.OutboxEventRepository;
import com.example.bankcards.outbox.service.OutboxService;
import com.example.bankcards.outbox.service.OutboxServiceImpl;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OutboxServiceTest {

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OutboxServiceImpl outboxService;

    @Test
    void shouldSaveUserRegisteredEvent() throws Exception {
        UserRegisteredPayload payload = new UserRegisteredPayload(
            1L,
            "alex@gmail.com",
            "Alex",
            "Kor"
        );

        String jsonPayload = """
                {
                    "userId": 1,
                    "email": "alex@gmail.com",
                    "firstname": "Alex",
                    "lastname": "Kor"
                }
                """;

        Mockito.when(objectMapper.writeValueAsString(payload))
                .thenReturn(jsonPayload);

        outboxService.saveEvent(
                AggregateType.USER,
                1L,
                EventType.USER_REGISTERED,
                payload
        );

        ArgumentCaptor<OutboxEvent> eventCaptor = ArgumentCaptor.forClass(OutboxEvent.class);

        Mockito.verify(outboxEventRepository).save(eventCaptor.capture());

        OutboxEvent savedEvent = eventCaptor.getValue();

        assertThat(savedEvent.getAggregateId()).isEqualTo(1L);
        assertThat(savedEvent.getEventStatus()).isEqualTo(EventStatus.NEW);
        assertThat(savedEvent.getEventType()).isEqualTo(EventType.USER_REGISTERED);
        assertThat(savedEvent.getAggregateType()).isEqualTo(AggregateType.USER);
        assertThat(savedEvent.getPayload()).isEqualTo(jsonPayload);
        assertThat(savedEvent.getEventId()).isNotNull();
        assertThat(savedEvent.getCreatedAt()).isNotNull();





    }

}
