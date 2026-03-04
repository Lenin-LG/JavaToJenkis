package com.bootcamp.transactions.application.service.dto;

import java.math.BigDecimal;

/*
    * DTO para la solicitud de un cargo en una cuenta.
 */
public record ChargeRequest(
        String accountId,
        BigDecimal amount,
        String currency,
        String description
) {}