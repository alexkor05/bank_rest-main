package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "User creation data")
public record CreateUserRequest(
        @Schema(description = "Firstname", example = "John")
        @NotBlank
        String firstname,

        @Schema(description = "Lastname", example = "Smith")
        @NotBlank
        String lastname,

        @Schema(description = "Birthdate", example = "1993-05-02")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Past
        LocalDate birthDate,

        @Schema(description = "Email", example = "johnsmith@gmail.com")
        @Email
        String email,

        @Schema(description = "Password")
        @NotBlank
        String password,

        @Schema(description = "Role", example = "USER")
        @NotNull
        Role role
) {}
