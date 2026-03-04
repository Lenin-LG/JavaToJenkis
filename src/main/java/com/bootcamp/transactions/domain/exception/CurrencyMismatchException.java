package com.bootcamp.transactions.domain.exception;

public class CurrencyMismatchException extends RuntimeException {
    public CurrencyMismatchException() {
        super("Currency mismatch");
    }
}