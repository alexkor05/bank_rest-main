package com.example.bankcards.controller;


import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.UpdateCardRequest;


import com.example.bankcards.service.CardServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Cards")
public class CardController {
    private final CardServiceImpl cardService;


    @Operation(operationId = "createCard", summary = "Create card", description = "Create card")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Card created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CardDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody @Valid CreateCardRequest createCardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cardService.createCard(createCardRequest));
    }



    @Operation(operationId = "findCard", summary = "Find card by Id", description = "Find card by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CardDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CardDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(cardService.findById(id));
    }


    @Operation(operationId = "updateCard", summary = "Update card by Id", description = "Update card by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card update",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CardDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(@RequestBody @Valid UpdateCardRequest updateCardRequest,
                                              @PathVariable Long id) {
        return ResponseEntity.ok().body(cardService.updateCard(id, updateCardRequest));
    }


    @Operation(operationId = "deleteCard", summary = "Delete card by Id", description = "Delete card by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Card deleted",
                    content = {}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(operationId = "transferMoney", summary = "Transfer of funds between the cards of the same user", description = "Transfer of funds between the cards of the same user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transfer successful ",
                    content = {}),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest transferRequest) {
        cardService.transfer(transferRequest);
        return ResponseEntity.noContent().build();
    }

}
