package com.bootcamp.transactions.domain.exception;
/*
    * Excepción personalizada para indicar que el balance es insuficiente para realizar una operación.
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Insufficient balance");
    }
}