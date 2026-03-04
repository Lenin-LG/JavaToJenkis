package com.bootcamp.transactions.domain.model;

import com.bootcamp.transactions.domain.exception.BusinessException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Operation {

    private final String operationId;
    private final String accountId;
    private final OperationType type;
    private final BigDecimal amount;
    private final String currency;
    private OperationStatus status;
    private final String reversedOperationId;
    private final LocalDateTime createdAt;

    private Operation(String operationId,
                      String accountId,
                      OperationType type,
                      BigDecimal amount,
                      String currency,
                      OperationStatus status,
                      String reversedOperationId,
                      LocalDateTime createdAt) {
        this.operationId = operationId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.reversedOperationId = reversedOperationId;
        this.createdAt = createdAt;
    }

    public static Operation charge(String accountId, BigDecimal amount, String currency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be positive");
        }
        return new Operation(
                UUID.randomUUID().toString(),
                accountId,
                OperationType.CHARGE,
                amount,
                currency,
                OperationStatus.APPLIED,
                null,
                LocalDateTime.now()
        );
    }

    public static Operation reversal(String accountId, BigDecimal amount, String currency, String originalOperationId) {
        return new Operation(
                UUID.randomUUID().toString(),
                accountId,
                OperationType.REVERSAL,
                amount,
                currency,
                OperationStatus.APPLIED,
                originalOperationId,
                LocalDateTime.now()
        );
    }
    public static Operation restore(
            String operationId,
            String accountId,
            OperationType type,
            BigDecimal amount,
            String currency,
            OperationStatus status,
            String reversedOperationId,
            LocalDateTime createdAt
    ) {
        return new Operation(
                operationId,
                accountId,
                type,
                amount,
                currency,
                status,
                reversedOperationId,
                createdAt
        );
    }
}