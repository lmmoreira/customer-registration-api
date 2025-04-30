package com.test.customer.infrastructure.repository;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.infrastructure.jpa.entities.JpaCustomer;
import com.test.customer.infrastructure.jpa.repositories.JpaCustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class CustomerRepositoryImpl implements CustomerRepository {

    private final JpaCustomerRepository jpaCustomerRepository;

    @Override
    public Optional<Customer> getById(Long id) {
        return jpaCustomerRepository.findById(id).map(JpaCustomer::toCustomer);
    }

    @Override
    public Optional<Customer> getByEmail(Email email) {
        return jpaCustomerRepository.findByEmail(email.value()).map(JpaCustomer::toCustomer);
    }

    @Override
    public List<Customer> getCustomers(int page, int size) {
        final var pageable = PageRequest.of(page, size);
        return jpaCustomerRepository.findAll(pageable).getContent().stream().map(JpaCustomer::toCustomer).collect(Collectors.toList());
    }

    @Override
    public long countCustomers() {
        return jpaCustomerRepository.count();
    }

    @Override
    @Transactional
    public Customer create(Customer customer) {
        return jpaCustomerRepository.save(JpaCustomer.of(customer)).toCustomer();
    }

    @Override
    @Transactional
    public Customer update(Customer customer) {
        return jpaCustomerRepository.save(JpaCustomer.of(customer)).toCustomer();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        jpaCustomerRepository.deleteById(id);
        ;
    }
}
