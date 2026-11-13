package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardSecurityService {

    private final TextEncryptor textEncryptor;

    public Card encrypt(Card card) {
        card.setCardNumber(textEncryptor.encrypt(card.getCardNumber()));
        return card;
    }

    public CardDto decrypt(CardDto cardDto) {
        cardDto.setCardNumber(textEncryptor.decrypt(cardDto.getCardNumber()));
        return cardDto;
    }


    public CardDto mask(CardDto cardDto) {
        cardDto.setCardNumber("**** **** **** " + cardDto.getCardNumber().substring(12));
        return cardDto;
    }

}
