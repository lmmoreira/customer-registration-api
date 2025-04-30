package com.test.customer.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    void shouldCreateCustomerSuccessfully() {
        Long id = 1L;
        String name = "John Doe";
        String email = "test@example.com";
        String phone = "+55 (12) 982553850";
        Address address = new Address("98121", "United States", "Seattle", "Washington");
        String password = "encodedPassword";
        String role = "USER";

        Customer customer = new Customer(id, name, Email.with(email), Telephone.with(phone), address, password, role);

        assertNotNull(customer);
        assertEquals(id, customer.id());
        assertEquals(name, customer.name());
        assertEquals(email, customer.email().value());
        assertEquals(phone, customer.phone().value());
        assertEquals(address, customer.address());
        assertEquals(password, customer.password());
        assertEquals(role, customer.role());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        String email = "test@example.com";
        String phone = "+55 (12) 982553850";
        Address address = new Address("98121", "United States", "Seattle", "Washington");

        assertThrows(IllegalArgumentException.class, () ->
                new Customer(1L, null, Email.with(email), Telephone.with(phone), address, "password", "USER")
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        String email = "test@example.com";
        String phone = "+55 (12) 982553850";
        Address address = new Address("98121", "United States", "Seattle", "Washington");

        assertThrows(IllegalArgumentException.class, () ->
                new Customer(1L, "   ", Email.with(email), Telephone.with(phone), address, "password", "USER")
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        String name = "John Doe";
        String phone = "+55 (12) 982553850";
        Address address = new Address("98121", "United States", "Seattle", "Washington");

        assertThrows(IllegalArgumentException.class, () ->
                new Customer(1L, name, null, Telephone.with(phone), address, "password", "USER")
        );
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsNull() {
        String name = "John Doe";
        String email = "test@example.com";
        Address address = new Address("98121", "United States", "Seattle", "Washington");

        assertThrows(IllegalArgumentException.class, () ->
                new Customer(1L, name, Email.with(email), null, address, "password", "USER")
        );
    }
}