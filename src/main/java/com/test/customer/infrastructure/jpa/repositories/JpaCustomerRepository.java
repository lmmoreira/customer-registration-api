package com.test.customer.infrastructure.jpa.repositories;

import com.test.customer.infrastructure.jpa.entities.JpaCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCustomerRepository extends JpaRepository<JpaCustomer, Long> {

    Optional<JpaCustomer> findByEmail(String email);

}
