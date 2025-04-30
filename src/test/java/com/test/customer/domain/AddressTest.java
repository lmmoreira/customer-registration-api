package com.test.customer.domain;

import com.test.customer.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    @Test
    void shouldCreateAddressSuccessfully() {
        String postCode = "12345";
        String country = "USA";
        String place = "New York";
        String state = "NY";

        Address address = new Address(postCode, country, place, state);

        assertNotNull(address);
        assertEquals(postCode, address.postCode());
        assertEquals(country, address.country());
        assertEquals(place, address.place());
        assertEquals(state, address.state());
    }

    @Test
    void shouldThrowValidationExceptionWhenPostCodeIsNull() {
        assertThrows(ValidationException.class, () -> new Address(null, "USA", "New York", "NY"));
    }

    @Test
    void shouldThrowValidationExceptionWhenPostCodeIsBlank() {
        assertThrows(ValidationException.class, () -> new Address("   ", "USA", "New York", "NY"));
    }

    @Test
    void shouldCreateAddressWithDefaultValuesUsingStaticMethod() {
        String postCode = "12345";

        Address address = Address.with(postCode);

        assertNotNull(address);
        assertEquals(postCode, address.postCode());
        assertNull(address.country());
        assertNull(address.place());
        assertNull(address.state());
    }
}