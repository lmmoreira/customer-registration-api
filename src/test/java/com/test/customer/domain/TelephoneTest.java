package com.test.customer.domain;

import com.test.customer.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TelephoneTest {

    @Test
    void shouldCreateTelephoneSuccessfully() {
        String validPhone = "+55 (12) 982553850";

        Telephone telephone = Telephone.with(validPhone);

        assertNotNull(telephone);
        assertEquals(validPhone, telephone.value());
    }

    @Test
    void shouldThrowValidationExceptionWhenTelephoneIsNull() {
        assertThrows(ValidationException.class, () -> Telephone.with(null));
    }

    @Test
    void shouldThrowValidationExceptionWhenTelephoneIsInvalid() {
        String invalidPhone = "123-456-7890";

        assertThrows(ValidationException.class, () -> Telephone.with(invalidPhone));
    }

    @Test
    void shouldThrowValidationExceptionWhenTelephoneIsBlank() {
        assertThrows(ValidationException.class, () -> Telephone.with("   "));
    }
}