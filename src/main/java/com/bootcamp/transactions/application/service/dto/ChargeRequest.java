package com.bootcamp.transactions.application.service.dto;

import java.math.BigDecimal;

public record ChargeRequest(
        String accountId,
        BigDecimal amount,
        String currency,
        String description
) {}