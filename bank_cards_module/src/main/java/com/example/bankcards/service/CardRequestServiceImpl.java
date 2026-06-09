package com.example.bankcards.service;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.AlreadyProcessedException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.RequestNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.mapper.CardRequestMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardRequestRepository;
import com.example.bankcards.util.CardSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CardRequestServiceImpl implements ICardRequestService {
    private final CardRequestRepository cardRequestRepository;
    private final CardRepository cardRepository;
    private final CardRequestMapper cardRequestMapper;
    private final KafkaProducerService kafkaProducerService;
    private final CardMapper cardMapper;

    @Transactional
    public CardRequestDto createRequest(CardRequestInfo cardRequestInfo) {

        Card card = cardRepository.findById(cardRequestInfo.cardId())
                .orElseThrow(() -> new EntityNotFoundException("Card with ID=" + cardRequestInfo.cardId() + " not found"));

        if(card.getStatus() != Status.ACTIVE && cardRequestInfo.requestType() == RequestType.BLOCK)
            throw new IllegalStateException("You can only block an active card.");
        if(card.getStatus() != Status.BLOCKED && cardRequestInfo.requestType() == RequestType.ACTIVATE)
            throw new IllegalStateException("You can only activate a blocked card.");

        Long countByCardIdAndStatus = cardRequestRepository.countByCardIdAndStatus(cardRequestInfo.cardId(), RequestStatus.PENDING);
        if(countByCardIdAndStatus > 0) throw new IllegalStateException("A request is already pending");

        CardRequest cardRequest = CardRequest.builder()
                .card(card)
                .user(card.getUser())
                .type(cardRequestInfo.requestType())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        CardRequest savedCardRequest = cardRequestRepository.save(cardRequest);
        return cardRequestMapper.toCardRequestDto(savedCardRequest);
    }

    @Cacheable(value = "cardRequests", key = "#id")
    public List<CardRequestDto> findRequestsByUserId(Long id){
        List<CardRequest> cardRequestList = cardRequestRepository.findByUserId(id);
        return cardRequestMapper.toCardRequestDtoList(cardRequestList);
    }

    public List<CardRequestDto> findPendingRequests() {
        List<CardRequest> cardRequestList = cardRequestRepository.findAllByStatus(RequestStatus.PENDING);
        return cardRequestMapper.toCardRequestDtoList(cardRequestList);
    }

    @Transactional
    public CardRequestDto processRequest(ProcessCardRequestDto processRequestDto) {

        CardRequest cardRequest = cardRequestRepository.findById(processRequestDto.requestId())
                .orElseThrow(() -> new RequestNotFoundException("Card request with ID = "
                        + processRequestDto.requestId() + " not found"));

        if(cardRequest.getStatus() != RequestStatus.PENDING) {
            throw new AlreadyProcessedException("Card request already processed");
        }

        if(processRequestDto.adminAction() == AdminAction.APPROVE) {
            Card card = cardRequest.getCard();
            String eventMessage = "";
            CardDto cardDto = cardMapper.toCardDto(card);
            CardSecurityUtils.decrypt(cardDto);
            CardSecurityUtils.mask(cardDto);

            if(cardRequest.getType() == RequestType.BLOCK) {
                card.setStatus(Status.BLOCKED);
                eventMessage = "Card " + cardDto.getCardNumber() + " was blocked";
            } else {
                card.setStatus(Status.ACTIVE);
                eventMessage = "Card " + cardDto.getCardNumber() + " was activated";
            }

            cardRepository.save(card);
            cardRequest.setStatus(RequestStatus.APPROVED);

            NotificationEvent event = new NotificationEvent(
                    card.getUser().getEmail(),
                    card.getUser().getFirstname(),
                    card.getUser().getLastname(),
                    cardRequest.getType() == RequestType.BLOCK ? EventType.CARD_BLOCKED : EventType.CARD_ACTIVATED,
                    eventMessage
            );
            kafkaProducerService.sendMessage(event);

        } else {
            cardRequest.setStatus(RequestStatus.REJECTED);
        }

        CardRequest savedRequest = cardRequestRepository.save(cardRequest);

        return cardRequestMapper.toCardRequestDto(savedRequest);
    }


}
