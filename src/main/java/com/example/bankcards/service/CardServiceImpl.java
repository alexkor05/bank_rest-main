package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.UpdateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.UserNotOwnerProvidedCardException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.UserPrincipal;
import com.example.bankcards.util.CardSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

        CardSecurityUtils.encrypt(card);

        Card createdCard = cardRepository.save(card);

        CardDto cardDto = cardMapper.toCardDto(createdCard);
        CardSecurityUtils.decrypt(cardDto);
        CardSecurityUtils.mask(cardDto);

        return cardDto;
    }

    @PostAuthorize("returnObject.userId == authentication.principal.id")
    @Override
    public CardDto findById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card with ID=" + id + " not found"));

        CardDto cardDto = cardMapper.toCardDto(card);

        CardSecurityUtils.decrypt(cardDto);
        CardSecurityUtils.mask(cardDto);

        return cardDto;

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
        CardSecurityUtils.encrypt(card);

        Card updatedCard = cardRepository.save(card);

        CardDto cardDto = cardMapper.toCardDto(updatedCard);

        CardSecurityUtils.decrypt(cardDto);
        CardSecurityUtils.mask(cardDto);

        return cardDto;
    }

    @Override
    @Transactional
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card with ID=" + id + " not found"));
        cardRepository.delete(card);
    }

    @Transactional
    @Override
    public void transfer(TransferRequest transferRequest) {

        Card fromCard = cardRepository.findById(transferRequest.fromCardId())
                .orElseThrow(() -> new EntityNotFoundException("Card with ID=" + transferRequest.fromCardId() + " not found"));

        Card toCard = cardRepository.findById(transferRequest.toCardId())
                .orElseThrow(() -> new EntityNotFoundException("Card with ID=" + transferRequest.toCardId() + " not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if(!userPrincipal.getId().equals(fromCard.getUser().getId())) {
            throw new UserNotOwnerProvidedCardException(
                    String.format("The user with ID = %s is not the owner of the provided card with ID = %s", userPrincipal.getId(), fromCard.getId()));
        }
        if(!userPrincipal.getId().equals(toCard.getUser().getId())) {
            throw new UserNotOwnerProvidedCardException(
                    String.format("The user with ID = %s is not the owner of the provided card with ID = %s", userPrincipal.getId(), toCard.getId()));
        }


        if(fromCard.getBalance().compareTo(transferRequest.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds on the card balance");
        }

        toCard.setBalance(toCard.getBalance().add(transferRequest.amount()));
        fromCard.setBalance(fromCard.getBalance().subtract(transferRequest.amount()));

        cardRepository.saveAll(List.of(fromCard, toCard));
    }
}
