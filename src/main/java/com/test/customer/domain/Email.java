package com.test.customer.domain;

import com.test.customer.domain.exception.ValidationException;

public record Email(String value) {

    private static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public Email {
        if (value == null || !value.matches(EMAIL)) {
            throw new ValidationException("Invalid value for Email");
        }
    }

    public static Email with(final String value) {
        return new Email(value);
    }

}
