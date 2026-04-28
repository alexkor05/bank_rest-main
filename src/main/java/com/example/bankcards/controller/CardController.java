package com.example.bankcards.controller;


import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.dto.UpdateCardRequest;
import com.example.bankcards.service.CardServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<CardDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(cardService.findById(id));
    }

    @PutMapping("/{id")
    public ResponseEntity<CardDto> updateCard(@RequestBody @Valid UpdateCardRequest updateCardRequest,
                                              @PathVariable Long id) {
        return ResponseEntity.ok().body(cardService.updateCard(id, updateCardRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

}
