package com.example.bankcards.dto;

import com.example.bankcards.entity.RequestStatus;
import com.example.bankcards.entity.RequestType;
import jakarta.validation.constraints.NotNull;

public record CardRequestInfo (
        @NotNull Long cardId,
        @NotNull RequestType requestType
        ){
}
