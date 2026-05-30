package com.example.bankcards.service;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.UserNotOwnerProvidedCardException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.UserPrincipal;
import com.example.bankcards.util.CardSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements ICardService{


    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;
    private final CardMapper cardMapper;


    @Override
    @Transactional
    public CardDto createCard(CreateCardRequest createCardRequest) {
        Card card = cardMapper.toCard(createCardRequest);
        User user = userRepository.findById(createCardRequest.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with Id = " + createCardRequest.userId() + " not found"));

        card.setUser(user);

        CardSecurityUtils.encrypt(card);

        Card createdCard = cardRepository.save(card);

        CardDto cardDto = cardMapper.toCardDto(createdCard);
        CardSecurityUtils.decrypt(cardDto);
        CardSecurityUtils.mask(cardDto);

        NotificationEvent event = new NotificationEvent(
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                EventType.CARD_CREATED,
                "Card with number " + cardDto.getCardNumber() + " was created successfully"
        );

        kafkaProducerService.sendMessage(event);

        return cardDto;
    }

    @PostAuthorize("returnObject.userId == authentication.principal.id or hasAuthority('ADMIN')")
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
        card.setExpiryDate(updateCardRequest.expiryDate());
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

        CardDto toCardDto = cardMapper.toCardDto(toCard);

        CardSecurityUtils.decrypt(toCardDto);
        CardSecurityUtils.mask(toCardDto);

        NotificationEvent event = new NotificationEvent(
                fromCard.getUser().getEmail(),
                fromCard.getUser().getFirstname(),
                fromCard.getUser().getLastname(),
                EventType.TRANSFER_COMPLETED,
                String.format("The money transfer was completed successfully.\n" +
                        "%.2f has been transferred to card %s. Balance %.2f.", transferRequest.amount(), toCardDto.getCardNumber(), toCardDto.getBalance())
        );

        kafkaProducerService.sendMessage(event);
    }
}
