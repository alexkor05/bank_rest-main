package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.dto.CardRequestInfo;

import java.util.List;

public interface ICardRequestService {
    CardRequestDto createRequest(CardRequestInfo cardRequestInfo);
    List<CardRequestDto> findRequestsByUserId(Long id);
}
