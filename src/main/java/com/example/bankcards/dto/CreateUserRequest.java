package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank String firstname,
        @NotBlank String lastname,
        @DateTimeFormat(pattern = "yyyy-MM-dd") @Past LocalDate birthDate,
        @Email  String email,
        @NotBlank String password,
        @NotNull Role role
) {}
