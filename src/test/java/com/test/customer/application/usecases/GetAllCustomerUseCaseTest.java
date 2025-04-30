package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.builders.CustomerTestBuilder;
import com.test.customer.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAllCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private GetAllCustomerUseCase getAllCustomerUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getAllCustomerUseCase = new GetAllCustomerUseCase(customerRepository);
    }

    @Test
    void shouldReturnAllCustomersSuccessfully() {
        int page = 0;
        int size = 10;

        Customer customer1 = CustomerTestBuilder.aCustomer()
                .withId(1L)
                .withName("John Doe")
                .build();

        Customer customer2 = CustomerTestBuilder.aCustomer()
                .withId(2L)
                .withName("Jane Doe")
                .build();

        List<Customer> customers = List.of(customer1, customer2);
        long totalElements = 2L;

        when(customerRepository.getCustomers(page, size)).thenReturn(customers);
        when(customerRepository.countCustomers()).thenReturn(totalElements);

        GetAllCustomerUseCase.Input input = new GetAllCustomerUseCase.Input(page, size);

        GetAllCustomerUseCase.Output output = getAllCustomerUseCase.execute(input);

        assertEquals(2, output.customers().size());
        assertEquals(totalElements, output.totalElements());

        GetAllCustomerUseCase.Output.Item item1 = output.customers().get(0);
        assertEquals(customer1.id(), item1.id());
        assertEquals(customer1.name(), item1.name());
        assertEquals(customer1.email().value(), item1.email());
        assertEquals(customer1.phone().value(), item1.phone());
        assertEquals(customer1.address().postCode(), item1.postCode());
        assertEquals(customer1.address().country(), item1.country());
        assertEquals(customer1.address().place(), item1.place());
        assertEquals(customer1.address().state(), item1.state());

        GetAllCustomerUseCase.Output.Item item2 = output.customers().get(1);
        assertEquals(customer2.id(), item2.id());
        assertEquals(customer2.name(), item2.name());
        assertEquals(customer2.email().value(), item2.email());
        assertEquals(customer2.phone().value(), item2.phone());
        assertEquals(customer2.address().postCode(), item2.postCode());
        assertEquals(customer2.address().country(), item2.country());
        assertEquals(customer2.address().place(), item2.place());
        assertEquals(customer2.address().state(), item2.state());

        verify(customerRepository, times(1)).getCustomers(page, size);
        verify(customerRepository, times(1)).countCustomers();
    }
}