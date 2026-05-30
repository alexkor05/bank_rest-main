package com.example.email.service;

import com.example.email.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;

    public void sendEmail(NotificationEvent event) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("alexkor0303@mail.ru");

        mailMessage.setSubject(event.eventType().getDescription());
        mailMessage.setTo(event.email());
        mailMessage.setText(event.message());
        mailSender.send(mailMessage);
    }
}
