package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;

public interface ICardService {
    CardDto createCard(CreateCardRequest createCardRequest);
}
