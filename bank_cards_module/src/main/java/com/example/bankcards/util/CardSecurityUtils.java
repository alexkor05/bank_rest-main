package com.example.bankcards.util;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.List;

public class CardSecurityUtils {

//    @Value("${security.card.pass}")
//    private static final CharSequence PASS;
//
//    @Value("${security.card.salt}")
//    private static final CharSequence SALT;

//    private static TextEncryptor textEncryptor = Encryptors.text(PASS, SALT);

//
//    private static TextEncryptor textEncryptor;
//
//    private CardSecurityUtils() {
//    }
//
//    public static Card encrypt(Card card) {
//        card.setCardNumber(textEncryptor.encrypt(card.getCardNumber()));
//        return card;
//    }
//
//    public static CardDto decrypt(CardDto cardDto) {
//        cardDto.setCardNumber(textEncryptor.decrypt(cardDto.getCardNumber()));
//        return cardDto;
//    }
//
//
//    public static CardDto mask(CardDto cardDto) {
//        cardDto.setCardNumber("**** **** **** " + cardDto.getCardNumber().substring(12));
//        return cardDto;
//    }



}
