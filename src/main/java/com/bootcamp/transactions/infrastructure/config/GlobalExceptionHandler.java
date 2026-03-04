package com.bootcamp.transactions.infrastructure.config;

import com.bootcamp.transactions.domain.exception.BusinessException;
import com.bootcamp.transactions.domain.exception.CurrencyMismatchException;
import com.bootcamp.transactions.domain.exception.InsufficientBalanceException;
import com.bootcamp.transactions.domain.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<String>> handleNotFound(NotFoundException ex) {
        return Mono.just(ResponseEntity.status(404).body(ex.getMessage()));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public Mono<ResponseEntity<String>> handleInsufficient(InsufficientBalanceException ex) {
        return Mono.just(ResponseEntity.status(409).body(ex.getMessage()));
    }

    @ExceptionHandler(CurrencyMismatchException.class)
    public Mono<ResponseEntity<String>> handleCurrency(CurrencyMismatchException ex) {
        return Mono.just(ResponseEntity.status(400).body(ex.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<String>> handleBusiness(BusinessException ex) {
        return Mono.just(ResponseEntity.status(400).body(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return Mono.just(ResponseEntity.status(500).body("Internal server error"));
    }
}