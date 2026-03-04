package com.bootcamp.transactions.domain.exception;

/*
    * Excepción personalizada para indicar que un recurso no fue encontrado.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}