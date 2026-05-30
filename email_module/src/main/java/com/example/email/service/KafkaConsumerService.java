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


//        System.out.println(pas);
//        System.out.println(event.email());
//        System.out.println(event.firstname());
//        System.out.println(event.lastname());
//        System.out.println(event.eventType().getDescription());
//        System.out.println(event.message());


    }
}
