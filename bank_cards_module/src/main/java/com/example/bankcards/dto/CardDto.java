package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Card DTO")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CardDto implements Serializable {
    @Schema(description = "Card ID", example = "12")
    private Long id;

    @Schema(description = "Masked card number", example = "**** **** **** 1077")
    private String cardNumber;

    @Schema(description = "Expired date", example = "2030-07-03")
    private LocalDate expiryDate;

    @Schema(description = "Card balance", example = "12000")
    private BigDecimal balance;

    @Schema(description = "Status", example = "ACTIVE")
    private Status status;

    @Schema(description = "User ID", example = "51")
    private Long userId;
}