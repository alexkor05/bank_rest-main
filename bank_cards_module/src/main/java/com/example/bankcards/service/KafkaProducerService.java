package com.example.bankcards.service;

import com.example.bankcards.dto.NotificationEvent;
import com.example.bankcards.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(NotificationEvent event){

        kafkaTemplate.send("email-notification-topic", event);
    }

}
