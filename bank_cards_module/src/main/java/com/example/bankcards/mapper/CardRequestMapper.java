package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.entity.CardRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardRequestMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "card.id", target = "cardId")
    CardRequestDto toCardRequestDto(CardRequest cardRequest);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "card.id", target = "cardId")
    List<CardRequestDto> toCardRequestDtoList(List<CardRequest> cardRequestList);
}
