package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.dto.ProcessCardRequestDto;
import com.example.bankcards.service.CardRequestServiceImpl;
import com.example.bankcards.service.ICardRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cards/requests")
public class AdminCardRequestController {
    private final ICardRequestService cardRequestService;

    @GetMapping
    public ResponseEntity<List<CardRequestDto>> getPendingRequest() {
        return ResponseEntity.ok().body(cardRequestService.findPendingRequests());
    }


    @PutMapping
    public ResponseEntity<CardRequestDto> processRequest(@RequestBody @Valid ProcessCardRequestDto processRequestDto) {
        CardRequestDto cardRequestDto = cardRequestService.processRequest(processRequestDto);
        return ResponseEntity.ok(cardRequestDto);
    }
}
