package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.application.gateway.ZipCodeGateway;
import com.test.customer.domain.Address;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.domain.exception.ValidationException;

public class CreateCustomerUseCase implements UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {

    private final CustomerRepository customerRepository;
    private final ZipCodeGateway zipCodeGateway;

    public CreateCustomerUseCase(CustomerRepository customerRepository, ZipCodeGateway zipCodeGateway) {
        this.customerRepository = customerRepository;
        this.zipCodeGateway = zipCodeGateway;
    }

    @Override
    public Output execute(Input input) {
        if (customerRepository.getByEmail(Email.with(input.email)).isPresent()) {
            throw new ValidationException("Customer already exists");
        }

        final Address address = zipCodeGateway.getByZipCode(input.zipCode).orElse(Address.with(input.zipCode));
        final Customer customer = customerRepository.create(Customer.createCustomer(input.name, input.email, input.phone, address, input.password, input.role));
        return Output.of(customer);
    }

    public record Input(String name, String email, String phone, String zipCode, String password, String role) {

    }

    public record Output(Long id, String name, String email, String phone, String postCode, String country, String place, String state) {

        static Output of(Customer customer) {
            return new Output(customer.id(), customer.name(), customer.email().value(), customer.phone().value(),
                    customer.address().postCode(), customer.address().country(), customer.address().place(), customer.address().state());
        }

    }


}
