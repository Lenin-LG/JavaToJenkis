package com.bootcamp.transactions.domain.exception;

/*
    * Excepción personalizada para errores de negocio.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}