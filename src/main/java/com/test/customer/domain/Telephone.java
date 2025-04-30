package com.test.customer.domain;

import com.test.customer.domain.exception.ValidationException;

public record Telephone(String value) {

    private static final String PHONE = "^\\+\\d{1,3} \\(\\d{1,4}\\) \\d{3,}$";

    public Telephone {
        if (value == null || !value.matches(PHONE)) {
            throw new ValidationException("Invalid value for Telephone");
        }
    }

    public static Telephone with(final String value) {
        return new Telephone(value);
    }

}
