package com.test.customer.config;

import com.test.customer.dto.request.RequestCustomerDTO;
import com.test.customer.service.customer.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartupConfig {

    @Bean
    public CommandLineRunner addCustomerOnStartup(CustomerService customerService, PasswordEncoder passwordEncoder) {
        return args -> {
            customerService.create(RequestCustomerDTO.builder()
                    .name("John Doe")
                    .email("johndoe@example.com")
                    .phone("+55 (12) 982553850")
                    .zipCode("12120")
                    .password("password")
                    .role("ADMIN").build());
        };
    }
}