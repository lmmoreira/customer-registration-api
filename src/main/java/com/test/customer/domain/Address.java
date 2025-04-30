package com.test.customer.domain;

import com.test.customer.domain.exception.ValidationException;

public record Address(String postCode, String country, String place, String state) {

    public Address {

        if (postCode == null || postCode.isBlank()) {
            throw new ValidationException("Postcode cannot be null or blank");
        }

    }

    public static Address with(final String value) {
        return new Address(value, null, null, null);
    }

}
