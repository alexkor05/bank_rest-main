package com.example.bankcards.dto;


import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.RequestStatus;
import com.example.bankcards.entity.RequestType;
import com.example.bankcards.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public record CardRequestDto(
        Long id,
        Long cardId,
        Long userId,
        RequestType type,
        RequestStatus status,
        LocalDateTime createdAt
        ) implements Serializable {
}
