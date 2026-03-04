package com.bootcamp.transactions.domain.exception;

/*
    * Excepción personalizada para indicar que hay una discrepancia en las monedas utilizadas.
 */
public class CurrencyMismatchException extends RuntimeException {
    public CurrencyMismatchException() {
        super("Currency mismatch");
    }
}