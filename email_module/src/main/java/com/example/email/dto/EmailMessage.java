package com.example.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class EmailMessage {
    private String subject;
    private String to;
    private String text;
}
