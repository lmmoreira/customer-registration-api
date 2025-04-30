package com.test.customer.infrastructure.config;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.domain.Address;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.domain.Telephone;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartupConfig {

    @Bean
    public CommandLineRunner addCustomerOnStartup(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Customer customer = new Customer(
                    null,
                    "John Doe",
                    new Email("johndoe@example.com"),
                    new Telephone("+55 (12) 982553850"),
                    new Address("12120", "Brazil", "Paulista", "SP"),
                    passwordEncoder.encode("password"),
                    "ADMIN"
            );
            customerRepository.create(customer);
        };
    }
}