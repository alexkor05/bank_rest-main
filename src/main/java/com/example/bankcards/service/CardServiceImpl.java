package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.dto.UpdateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements ICardService{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    @Override
    @Transactional
    public CardDto createCard(CreateCardRequest createCardRequest) {
        Card card = cardMapper.toCard(createCardRequest);

        card.setUser(userRepository.findById(createCardRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with Id = " + createCardRequest.userId() + " not found")));

        Card createdCard = cardRepository.save(card);
        return cardMapper.toCardDto(createdCard);
    }

    @Override
    public CardDto findById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card with ID=" + id + " not found"));
        return cardMapper.toCardDto(card);
    }

    @Override
    @Transactional
    public CardDto updateCard(Long id, UpdateCardRequest updateCardRequest) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card with ID=" + id + " not found"));
        card.setCardNumber(updateCardRequest.cardNumber());
        card.setStatus(updateCardRequest.status());
        card.setBalance(updateCardRequest.balance());
        card.setExpiredDate(updateCardRequest.expiredDate());

        return cardMapper.toCardDto(cardRepository.save(card));
    }

    @Override
    @Transactional
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card with ID=" + id + " not found"));
        cardRepository.delete(card);
    }
}
