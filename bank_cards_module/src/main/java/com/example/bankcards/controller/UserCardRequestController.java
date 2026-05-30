package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.dto.CardRequestInfo;
import com.example.bankcards.security.UserPrincipal;
import com.example.bankcards.service.CardRequestServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards/requests")
@RequiredArgsConstructor
public class UserCardRequestController {

    private final CardRequestServiceImpl cardRequestService;

    @GetMapping()
    public ResponseEntity<List<CardRequestDto>> getMyRequests(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<CardRequestDto> cardRequestDtoList = cardRequestService.findRequestsByUserId(userPrincipal.getId());
        return ResponseEntity.ok(cardRequestDtoList);
    }

    @PostMapping()
    public ResponseEntity<CardRequestDto> createCardRequest(@Valid @RequestBody CardRequestInfo cardRequestInfo) {
        CardRequestDto cardRequestDto = cardRequestService.createRequest(cardRequestInfo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cardRequestDto);
    }

}
