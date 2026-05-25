package com.example.bankcards.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
@Schema(description = "Transfer data")
public record TransferRequest (
        @Schema(description = "ID of the card that is being transferred from", example = "12")
        @NotNull
        Long fromCardId,

        @Schema(description = "ID of the card being transferred to", example = "22")
        @NotNull
        Long toCardId,

        @Schema(description = "Transfer amount", example = "5000")
        @NotNull
        BigDecimal amount,

        @Schema(description = "User id", example = "101")
        @NotNull
        Long userId
) {
}
