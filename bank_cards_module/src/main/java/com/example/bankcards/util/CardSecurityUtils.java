package com.example.bankcards.util;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.List;

public class CardSecurityUtils {
    private static final CharSequence PASS = "Samsung";
    private static final CharSequence SALT = "a28ad94dde798a004e4d";
    private static TextEncryptor textEncryptor = Encryptors.text(PASS, SALT);

    private CardSecurityUtils() {
    }

    public static Card encrypt(Card card) {
        card.setCardNumber(textEncryptor.encrypt(card.getCardNumber()));
        return card;
    }

    public static CardDto decrypt(CardDto cardDto) {
        cardDto.setCardNumber(textEncryptor.decrypt(cardDto.getCardNumber()));
        return cardDto;
    }


    public static CardDto mask(CardDto cardDto) {
        cardDto.setCardNumber("**** **** **** " + cardDto.getCardNumber().substring(12));
        return cardDto;
    }



}
