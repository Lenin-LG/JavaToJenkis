package com.bootcamp.transactions.application.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
