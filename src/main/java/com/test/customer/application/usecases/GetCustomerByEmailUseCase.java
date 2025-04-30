package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.domain.exception.CustomerNotFoundException;

public class GetCustomerByEmailUseCase implements UseCase<GetCustomerByEmailUseCase.Input, GetCustomerByEmailUseCase.Output> {

    private final CustomerRepository customerRepository;

    public GetCustomerByEmailUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public GetCustomerByEmailUseCase.Output execute(GetCustomerByEmailUseCase.Input input) {
        return GetCustomerByEmailUseCase.Output.of(customerRepository.getByEmail(Email.with(input.email()))
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found")));
    }

    public record Input(String email) {
    }

    public record Output(Long id, String name, String email, String phone, String postCode, String country,
                         String place, String state) {
        static GetCustomerByEmailUseCase.Output of(Customer customer) {
            return new GetCustomerByEmailUseCase.Output(customer.id(), customer.name(), customer.email().value(), customer.phone().value(),
                    customer.address().postCode(), customer.address().country(), customer.address().place(), customer.address().state());
        }
    }
}
