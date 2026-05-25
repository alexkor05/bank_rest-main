package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshRequest(
        @NotNull
        String refreshToken
) {}
