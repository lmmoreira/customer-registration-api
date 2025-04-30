package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.domain.exception.ValidationException;

public class DeleteCustomerUseCase implements VoidUseCase<DeleteCustomerUseCase.Input> {

    private final CustomerRepository customerRepository;

    public DeleteCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void execute(DeleteCustomerUseCase.Input input) {
        if (customerRepository.getById(input.id).isEmpty()) {
            throw new ValidationException("Customer not found");
        }

        customerRepository.delete(input.id);
    }

    public record Input(Long id) {

    }

}
