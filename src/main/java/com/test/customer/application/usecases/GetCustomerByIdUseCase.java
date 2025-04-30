package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.domain.Customer;
import com.test.customer.domain.exception.CustomerNotFoundException;

public class GetCustomerByIdUseCase implements UseCase<GetCustomerByIdUseCase.Input, GetCustomerByIdUseCase.Output> {

    private final CustomerRepository customerRepository;

    public GetCustomerByIdUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public GetCustomerByIdUseCase.Output execute(GetCustomerByIdUseCase.Input input) {
        return Output.of(customerRepository.getById(input.id())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found")));
    }

    public record Input(Long id) {
    }

    public record Output(Long id, String name, String email, String phone, String postCode, String country, String place, String state) {
        static GetCustomerByIdUseCase.Output of(Customer customer) {
            return new GetCustomerByIdUseCase.Output(customer.id(), customer.name(), customer.email().value(), customer.phone().value(),
                    customer.address().postCode(), customer.address().country(), customer.address().place(), customer.address().state());
        }
    }

}
