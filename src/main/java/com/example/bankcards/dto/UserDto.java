package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;

import java.time.LocalDate;
import java.util.List;

public record UserDto (
        Long id,
        String firstname,
        String lastname,
        LocalDate birthDate,
        String email,
        Role role,
        List<CardDto> cards
){}
