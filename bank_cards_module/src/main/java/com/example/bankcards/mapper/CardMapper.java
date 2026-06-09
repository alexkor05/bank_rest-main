package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "user.id", target = "userId")
    CardDto toCardDto(Card card);

    @Mapping(target = "user", ignore = true)
    Card toCard(CreateCardRequest createCardRequest);
}
