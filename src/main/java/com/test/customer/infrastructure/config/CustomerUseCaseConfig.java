package com.test.customer.infrastructure.config;

import com.test.customer.application.gateway.ZipCodeGateway;
import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.application.usecases.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class CustomerUseCaseConfig {

    private final CustomerRepository customerRepository;
    private final ZipCodeGateway zipCodeGateway;

    @Bean
    public CreateCustomerUseCase getCreateCustomerUseCase() {
        return new CreateCustomerUseCase(customerRepository, zipCodeGateway);
    }

    @Bean
    public UpdateCustomerUseCase getUpdateCustomerUseCase() {
        return new UpdateCustomerUseCase(customerRepository, zipCodeGateway);
    }

    @Bean
    public GetCustomerByEmailUseCase getGetCustomerByEmailUseCase() {
        return new GetCustomerByEmailUseCase(customerRepository);
    }

    @Bean
    public GetCustomerByIdUseCase getGetCustomerByIdUseCase() {
        return new GetCustomerByIdUseCase(customerRepository);
    }

    @Bean
    public GetAllCustomerUseCase getGetAllCustomerUseCase() {
        return new GetAllCustomerUseCase(customerRepository);
    }

    @Bean
    public DeleteCustomerUseCase getDeleteCustomerUseCase() {
        return new DeleteCustomerUseCase(customerRepository);
    }

}
