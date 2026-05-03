package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "User data")
public record UserDto (

        @Schema(description = "User ID", example = "14")
        Long id,

        @Schema(description = "Firstname", example = "John")
        String firstname,

        @Schema(description = "Lastname", example = "Smith")
        String lastname,

        @Schema(description = "Birthdate", example = "1993-05-02")
        LocalDate birthDate,

        @Schema(description = "Email", example = "johnsmith@gmail.com")
        String email,

        @Schema(description = "Role", example = "USER")
        Role role,

        @Schema(description = "Cards list")
        List<CardDto> cards
){}
