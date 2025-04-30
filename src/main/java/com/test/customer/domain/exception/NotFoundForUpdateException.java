package com.test.customer.domain.exception;

public class NotFoundForUpdateException extends DomainException {

    public NotFoundForUpdateException(String message) {
        super(message);
    }

    public NotFoundForUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

}