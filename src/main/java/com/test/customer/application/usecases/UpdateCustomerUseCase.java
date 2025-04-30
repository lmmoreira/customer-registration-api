package com.test.customer.application.usecases;

import com.test.customer.application.gateway.ZipCodeGateway;
import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.domain.Address;
import com.test.customer.domain.Customer;
import com.test.customer.domain.exception.NotFoundForUpdateException;

public class UpdateCustomerUseCase implements UseCase<UpdateCustomerUseCase.Input, UpdateCustomerUseCase.Output> {

    private final CustomerRepository customerRepository;
    private final ZipCodeGateway zipCodeGateway;

    public UpdateCustomerUseCase(CustomerRepository customerRepository, ZipCodeGateway zipCodeGateway) {
        this.customerRepository = customerRepository;
        this.zipCodeGateway = zipCodeGateway;
    }

    @Override
    public UpdateCustomerUseCase.Output execute(UpdateCustomerUseCase.Input input) {
        if (customerRepository.getById(input.id).isEmpty()) {
            throw new NotFoundForUpdateException("Customer not found for update");
        }

        final Address address = zipCodeGateway.getByZipCode(input.zipCode).orElse(Address.with(input.zipCode));
        final Customer customer = customerRepository.update(Customer.createCustomer(input.id, input.name, input.email, input.phone, address, input.password, input.role));
        return UpdateCustomerUseCase.Output.of(customer);
    }

    public record Input(Long id, String name, String email, String phone, String zipCode, String password, String role) {

    }

    public record Output(Long id, String name, String email, String phone, String postCode, String country,
                         String place, String state) {

        static UpdateCustomerUseCase.Output of(Customer customer) {
            return new UpdateCustomerUseCase.Output(customer.id(), customer.name(), customer.email().value(), customer.phone().value(),
                    customer.address().postCode(), customer.address().country(), customer.address().place(), customer.address().state());
        }

    }

}
