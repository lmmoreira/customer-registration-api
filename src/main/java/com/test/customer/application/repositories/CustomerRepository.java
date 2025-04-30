package com.test.customer.application.repositories;

import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Optional<Customer> getById(Long id);

    Optional<Customer> getByEmail(Email email);

    Customer create(Customer customer);

    Customer update(Customer customer);

    void delete(Long id);

    List<Customer> getCustomers(int page, int size);

    long countCustomers();

}