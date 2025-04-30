package com.test.customer.infrastructure.config;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.application.usecases.AuthenticationUseCase;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
public class AuthenticationUseCaseConfig {

    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationUseCase getAuthenticationUseCase() {
        return new AuthenticationUseCase(customerRepository, passwordEncoder);
    }

}
