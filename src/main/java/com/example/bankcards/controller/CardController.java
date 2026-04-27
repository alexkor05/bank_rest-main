package com.example.bankcards.controller;


import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.service.CardServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardServiceImpl cardService;

    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody @Valid CreateCardRequest createCardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cardService.createCard(createCardRequest));
    }
}
