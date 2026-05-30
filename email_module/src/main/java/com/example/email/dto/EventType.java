package com.example.email.dto;

public enum EventType {
    USER_REGISTERED("Registration"),
    CARD_CREATED("Created a bank card"),
    TRANSFER_COMPLETED("Transfer completed"),
    CARD_BLOCKED("The card is blocked"),
    CARD_ACTIVATED("the card is activated");

    private final String description;

    EventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
