package com.example.bankcards.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode status,
                                                                            WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Validation failed");
        problemDetail.setTitle("Invalid payload request");
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> Map.of("field", fieldError.getField(), "message", fieldError.getDefaultMessage()))
                .toList();

        problemDetail.setProperty("errors", errors);
        return ResponseEntity.status(status).body(problemDetail);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail entityNotFoundExceptionHandler(EntityNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource not found");
        return problemDetail;
    }

    @ExceptionHandler(UserNotOwnerProvidedCardException.class)
    public ProblemDetail userNotOwnerProvidedCardExceptionHandler(UserNotOwnerProvidedCardException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Bad request");
        return problemDetail;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail ExpiredJwtExceptionHandler(ExpiredJwtException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Jwt access token has expired");
        return problemDetail;
    }


}
