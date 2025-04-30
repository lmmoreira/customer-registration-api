package com.test.customer.domain;

public record Customer(Long id, String name, Email email, Telephone phone, Address address, String password,
                       String role) {

    public Customer {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid value for Name");
        }

        if (email == null) {
            throw new IllegalArgumentException("Invalid value for Email");
        }

        if (phone == null) {
            throw new IllegalArgumentException("Invalid value for Phone");
        }

    }

    public static Customer createCustomer(String name, String email, String phone, Address address, String password, String role) {
        return new Customer(null, name, Email.with(email), Telephone.with(phone), address, password, role);
    }

    public static Customer createCustomer(Long id, String name, String email, String phone, Address address, String password, String role) {
        return new Customer(id, name, Email.with(email), Telephone.with(phone), address, password, role);
    }

}
