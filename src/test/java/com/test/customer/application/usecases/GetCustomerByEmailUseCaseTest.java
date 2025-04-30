package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.builders.CustomerTestBuilder;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.domain.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetCustomerByEmailUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    private GetCustomerByEmailUseCase getCustomerByEmailUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getCustomerByEmailUseCase = new GetCustomerByEmailUseCase(customerRepository);
    }

    @Test
    void shouldReturnCustomerSuccessfully() {
        String email = "test@example.com";

        Customer customer = CustomerTestBuilder.aCustomer()
                .withEmail(Email.with(email))
                .build();

        when(customerRepository.getByEmail(Email.with(email))).thenReturn(Optional.of(customer));

        GetCustomerByEmailUseCase.Input input = new GetCustomerByEmailUseCase.Input(email);

        GetCustomerByEmailUseCase.Output output = getCustomerByEmailUseCase.execute(input);

        assertNotNull(output);
        assertEquals(customer.id(), output.id());
        assertEquals(customer.name(), output.name());
        assertEquals(customer.email().value(), output.email());
        assertEquals(customer.phone().value(), output.phone());
        assertEquals(customer.address().postCode(), output.postCode());
        assertEquals(customer.address().country(), output.country());
        assertEquals(customer.address().place(), output.place());
        assertEquals(customer.address().state(), output.state());
        verify(customerRepository, times(1)).getByEmail(Email.with(email));
    }

    @Test
    void shouldThrowCustomerNotFoundExceptionWhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";

        when(customerRepository.getByEmail(Email.with(email))).thenReturn(Optional.empty());

        GetCustomerByEmailUseCase.Input input = new GetCustomerByEmailUseCase.Input(email);

        assertThrows(CustomerNotFoundException.class, () -> getCustomerByEmailUseCase.execute(input));
        verify(customerRepository, times(1)).getByEmail(Email.with(email));
    }
}