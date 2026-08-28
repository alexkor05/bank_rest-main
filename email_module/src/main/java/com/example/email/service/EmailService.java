package com.example.email.service;

import com.example.email.dto.BankEvent;
import com.example.email.dto.CardActivatedPayload;
import com.example.email.dto.CardBlockedPayload;
import com.example.email.dto.CardCreatedPayload;
import com.example.email.dto.EmailMessage;
import com.example.email.dto.TransferCompletedPayload;
import com.example.email.dto.UserRegisteredPayload;
import com.example.email.entity.ProcessedEvent;
import com.example.email.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(EmailService.class.getName());
    private final ProcessedEventRepository processedEventRepository;

    @Value("${spring.mail.username}")
    private String sender;

    public void process(BankEvent event) {
        EmailMessage message = new EmailMessage();
        if(processedEventRepository.existsById(event.eventId())) {
            log.info("Event {} has already been processed", event.eventId());
            return;
        }

        switch (event.eventType()) {
            case USER_REGISTERED -> {
                UserRegisteredPayload userRegisteredPayload = objectMapper.treeToValue(event.payload(), UserRegisteredPayload.class);
                message.setTo(userRegisteredPayload.email());
                message.setSubject("User registration");
                message.setText(userRegisteredPayload.firstName() + " " + userRegisteredPayload.lastName()
                        + ", your registration has been successfully confirmed.");
            }
            case CARD_CREATED -> {
                CardCreatedPayload cardCreatedPayload = objectMapper.treeToValue(event.payload(), CardCreatedPayload.class);
                message.setTo(cardCreatedPayload.email());
                message.setSubject("Card created");
                message.setText("Card with number " + cardCreatedPayload.maskedCardNumber() + " was created successfully");
            }
            case TRANSFER_COMPLETED -> {
                TransferCompletedPayload transferCompletedPayload = objectMapper.treeToValue(event.payload(), TransferCompletedPayload.class);
                message.setTo(transferCompletedPayload.email());
                message.setSubject("Transfer completed");
                message.setText(
                        String.format("The money transfer was completed successfully.\n" +
                                "%.2f has been transferred from card %s to card %s",
                                transferCompletedPayload.amount(),
                                transferCompletedPayload.fromMaskedCardNumber(),
                                transferCompletedPayload.toMaskedCardNumber())
                );
            }
            case CARD_ACTIVATED -> {
                CardActivatedPayload cardActivatedPayload = objectMapper.treeToValue(event.payload(), CardActivatedPayload.class);
                message.setTo(cardActivatedPayload.email());
                message.setSubject("Card activated");
                message.setText( "Card " + cardActivatedPayload.maskedCardNumber() + " was activated");
            }
            case CARD_BLOCKED -> {
                CardBlockedPayload cardBlockedPayload= objectMapper.treeToValue(event.payload(), CardBlockedPayload.class);
                message.setTo(cardBlockedPayload.email());
                message.setSubject("Card blocked");
                message.setText( "Card " + cardBlockedPayload.maskedCardNumber() + " was blocked");
            }

            default -> {
                log.warn("Event type {} not supported.", event.eventType());
                return;
            }
        }


        sendEmail(message);
        ProcessedEvent processedEvent = new ProcessedEvent();
        processedEvent.setEventId(event.eventId());
        processedEvent.setProcessedAt(LocalDateTime.now());
        processedEventRepository.save(processedEvent);

    }

    public void sendEmail(EmailMessage message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);

        mailMessage.setSubject(message.getSubject());
        mailMessage.setTo(message.getTo());
        mailMessage.setText(message.getText());

        mailSender.send(mailMessage);
    }
}
