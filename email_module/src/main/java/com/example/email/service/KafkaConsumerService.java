package com.example.email.service;


import com.example.email.dto.BankEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {


    private final EmailService emailService;


    @KafkaListener(topics = "${app.kafka.topics.bankEvents}", groupId = "email-service-group")
    public void listener(BankEvent event) {

        emailService.process(event);

    }
}
