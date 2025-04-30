package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.builders.CustomerTestBuilder;
import com.test.customer.domain.Customer;
import com.test.customer.domain.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetCustomerByIdUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private GetCustomerByIdUseCase getCustomerByIdUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getCustomerByIdUseCase = new GetCustomerByIdUseCase(customerRepository);
    }

    @Test
    void shouldReturnCustomerSuccessfully() {
        Long customerId = 1L;

        Customer customer = CustomerTestBuilder.aCustomer()
                .withId(customerId)
                .build();

        when(customerRepository.getById(customerId)).thenReturn(Optional.of(customer));

        GetCustomerByIdUseCase.Input input = new GetCustomerByIdUseCase.Input(customerId);

        GetCustomerByIdUseCase.Output output = getCustomerByIdUseCase.execute(input);

        assertNotNull(output);
        assertEquals(customer.id(), output.id());
        assertEquals(customer.name(), output.name());
        assertEquals(customer.email().value(), output.email());
        assertEquals(customer.phone().value(), output.phone());
        assertEquals(customer.address().postCode(), output.postCode());
        assertEquals(customer.address().country(), output.country());
        assertEquals(customer.address().place(), output.place());
        assertEquals(customer.address().state(), output.state());
        verify(customerRepository, times(1)).getById(customerId);
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenIdDoesNotExist() {
        Long customerId = 1L;

        when(customerRepository.getById(customerId)).thenReturn(Optional.empty());

        GetCustomerByIdUseCase.Input input = new GetCustomerByIdUseCase.Input(customerId);

        assertThrows(CustomerNotFoundException.class, () -> getCustomerByIdUseCase.execute(input));
        verify(customerRepository, times(1)).getById(customerId);
    }
}