package com.test.customer.service.exception;

public class AuthException extends DomainException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

}