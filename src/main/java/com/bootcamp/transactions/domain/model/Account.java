package com.bootcamp.transactions.domain.model;

import com.bootcamp.transactions.domain.exception.BusinessException;
import com.bootcamp.transactions.domain.exception.InsufficientBalanceException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Account {

    private String accountId;
    private String currency;
    private BigDecimal balance;
    private LocalDateTime updatedAt;
    private Long version;

    public Account(String accountId,
                   String currency,
                   BigDecimal balance,
                   LocalDateTime updatedAt,
                   Long version) {

        this.accountId = accountId;
        this.currency = currency;
        this.balance = balance;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public void applyCharge(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Amount must be positive");
        }

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }
        this.balance = balance.subtract(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public void applyReversal(BigDecimal amount) {
        this.balance = balance.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

}