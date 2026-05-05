package com.example.bankcards.exception;

public class UserNotOwnerProvidedCardException extends RuntimeException{
    public UserNotOwnerProvidedCardException(String message) {
        super(message);
    }
}
