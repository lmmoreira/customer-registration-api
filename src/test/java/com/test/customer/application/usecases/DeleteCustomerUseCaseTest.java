package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.domain.Customer;
import com.test.customer.domain.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeleteCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private DeleteCustomerUseCase deleteCustomerUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteCustomerUseCase = new DeleteCustomerUseCase(customerRepository);
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        Long customerId = 1L;

        when(customerRepository.getById(customerId)).thenReturn(Optional.of(mock(Customer.class)));

        DeleteCustomerUseCase.Input input = new DeleteCustomerUseCase.Input(customerId);

        deleteCustomerUseCase.execute(input);

        verify(customerRepository, times(1)).getById(customerId);
        verify(customerRepository, times(1)).delete(customerId);
    }

    @Test
    void shouldThrowValidationExceptionWhenCustomerNotFound() {
        Long customerId = 1L;

        when(customerRepository.getById(customerId)).thenReturn(Optional.empty());

        DeleteCustomerUseCase.Input input = new DeleteCustomerUseCase.Input(customerId);

        assertThrows(ValidationException.class, () -> deleteCustomerUseCase.execute(input));
        verify(customerRepository, times(1)).getById(customerId);
        verifyNoMoreInteractions(customerRepository);
    }
}