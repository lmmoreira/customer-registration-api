package com.test.customer.service.exception;

public class CustomerNotFoundException extends DomainException {

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}