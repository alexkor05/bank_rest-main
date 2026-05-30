package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Card creation data")
public record CreateCardRequest(
        @Schema(description = "Card number", example = "1234 5678 9123 4567")
        @NotBlank @Size(min = 16, max = 16)
        String cardNumber,

        @Schema(description = "Card expiration date", example = "2031-02-10")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Future
        LocalDate expiryDate,

        @Schema(description = "Card balance", example = "12000")
        @NotNull
        BigDecimal balance,

        @Schema(description = "Card status", example = "ACTIVE")
        @NotNull
        Status status,

        @Schema(description = "User id", example = "101")
        @NotNull
        Long userId
) {
}
