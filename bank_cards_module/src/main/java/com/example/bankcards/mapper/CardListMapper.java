package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = CardMapper.class)
public interface CardListMapper {
    List<CardDto> toCardDtoList(List<Card> cards);
}
