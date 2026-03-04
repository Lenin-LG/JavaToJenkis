package com.bootcamp.transactions.application.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*
    * DTO para la respuesta de una operación realizada en una cuenta.
 */
public record OperationResponse(
        String operationId,
        String reversedOperationId,
        String accountId,
        String type,
        BigDecimal amount,
        String currency,
        String status,
        BigDecimal balanceAfter,
        LocalDateTime createdAt
) {}
