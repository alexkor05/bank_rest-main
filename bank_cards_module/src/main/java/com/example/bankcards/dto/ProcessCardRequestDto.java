package com.example.bankcards.dto;

import com.example.bankcards.entity.AdminAction;
import jakarta.validation.constraints.NotNull;

public record ProcessCardRequestDto (
        @NotNull Long requestId,
        @NotNull AdminAction adminAction
){
}
