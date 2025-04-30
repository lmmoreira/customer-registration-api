package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;

import java.util.List;

public class GetAllCustomerUseCase implements UseCase<GetAllCustomerUseCase.Input, GetAllCustomerUseCase.Output> {

    private final CustomerRepository customerRepository;

    public GetAllCustomerUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Output execute(Input input) {
        final var customers = customerRepository.getCustomers(input.page(), input.size());
        final var totalElements = customerRepository.countCustomers();

        return new Output(customers.stream().map(customer -> new Output.Item(
                customer.id(),
                customer.name(),
                customer.email().value(),
                customer.phone().value(),
                customer.address().postCode(),
                customer.address().country(),
                customer.address().place(),
                customer.address().state()
        )).toList(), totalElements);
    }

    public record Input(int page, int size) {
    }

    public record Output(List<Item> customers, long totalElements) {
        public record Item(Long id, String name, String email, String phone, String postCode, String country, String place, String state) {
        }
    }
}