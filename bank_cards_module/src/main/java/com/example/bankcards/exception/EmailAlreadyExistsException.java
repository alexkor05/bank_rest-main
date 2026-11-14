package com.example.bankcards.exception;


public class EmailAlreadyExistsException extends RuntimeException {
    private final String email;
    public EmailAlreadyExistsException(String email) {
        super("Email %s is already registered".formatted(email));
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
}
