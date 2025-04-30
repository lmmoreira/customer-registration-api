package com.test.customer.builders;

import com.test.customer.domain.Address;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.domain.Telephone;

public class CustomerTestBuilder {

    private Long id = 1L;
    private String name = "John Doe";
    private Email email = Email.with("test@example.com");
    private Telephone phone = Telephone.with("+55 (12) 982553850");
    private Address address = new Address("98121", "United States", "Seattle", "Washington");
    private String password = "encodedPassword";
    private String role = "USER";

    private CustomerTestBuilder() {
    }

    public static CustomerTestBuilder aCustomer() {
        return new CustomerTestBuilder();
    }

    public CustomerTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CustomerTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CustomerTestBuilder withEmail(Email email) {
        this.email = email;
        return this;
    }

    public CustomerTestBuilder withPhone(Telephone phone) {
        this.phone = phone;
        return this;
    }

    public CustomerTestBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    public CustomerTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public CustomerTestBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    public Customer build() {
        return new Customer(id, name, email, phone, address, password, role);
    }
}