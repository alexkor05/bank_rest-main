package com.example.email.service;


import com.example.email.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {


    private final EmailService emailService;

    @KafkaListener(topics = "email-notification-topic", groupId = "email-service-group")
    public void listener(NotificationEvent event) {

        emailService.sendEmail(event);

    }
}
