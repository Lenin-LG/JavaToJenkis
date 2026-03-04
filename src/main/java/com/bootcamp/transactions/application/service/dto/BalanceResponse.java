package com.bootcamp.transactions.application.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/*
    * DTO para la respuesta del balance de una cuenta.
 */
public record BalanceResponse(
        String accountId,
        String currency,
        BigDecimal balance,
        LocalDateTime updatedAt
) {}
