package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements ICardService{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    @Override
    public CardDto createCard(CreateCardRequest createCardRequest) {
        Card card = cardMapper.toCard(createCardRequest);

        card.setUser(userRepository.findById(createCardRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with Id = " + createCardRequest.userId() + " not found")));

        Card createdCard = cardRepository.save(card);
        return cardMapper.toCardDto(createdCard);
    }
}
