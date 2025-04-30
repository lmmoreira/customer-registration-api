package com.test.customer.domain;

import com.test.customer.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    void shouldCreateEmailSuccessfully() {
        String validEmail = "test@example.com";

        Email email = Email.with(validEmail);

        assertNotNull(email);
        assertEquals(validEmail, email.value());
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsNull() {
        assertThrows(ValidationException.class, () -> Email.with(null));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsInvalid() {
        String invalidEmail = "invalid-email";

        assertThrows(ValidationException.class, () -> Email.with(invalidEmail));
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsBlank() {
        assertThrows(ValidationException.class, () -> Email.with("   "));
    }
}