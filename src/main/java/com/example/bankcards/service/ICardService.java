package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.dto.UpdateCardRequest;

public interface ICardService {
    CardDto createCard(CreateCardRequest createCardRequest);
    CardDto findById(Long id);
    CardDto updateCard(Long id, UpdateCardRequest updateCardRequest);
    void delete(Long id);
}
