package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.builders.CustomerTestBuilder;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.domain.exception.CustomerNotFoundException;
import com.test.customer.domain.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthenticationUseCase authenticationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationUseCase = new AuthenticationUseCase(customerRepository, passwordEncoder);
    }

    @Test
    void shouldAuthenticateSuccessfully() {
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";

        Customer customer = CustomerTestBuilder.aCustomer()
                .withEmail(Email.with(email))
                .withPassword(encodedPassword)
                .build();

        when(customerRepository.getByEmail(Email.with(email))).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        AuthenticationUseCase.Input input = new AuthenticationUseCase.Input(email, password);

        AuthenticationUseCase.Output output = authenticationUseCase.execute(input);

        assertNotNull(output);
        assertEquals(customer.id(), output.id());
        assertEquals(customer.name(), output.name());
        assertEquals(customer.email().value(), output.email());
        verify(customerRepository, times(1)).getByEmail(Email.with(email));
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void shouldThrowCustomerNotFoundException() {
        String email = "nonexistent@example.com";
        String password = "password";

        when(customerRepository.getByEmail(Email.with(email))).thenReturn(Optional.empty());

        AuthenticationUseCase.Input input = new AuthenticationUseCase.Input(email, password);

        assertThrows(CustomerNotFoundException.class, () -> authenticationUseCase.execute(input));
        verify(customerRepository, times(1)).getByEmail(Email.with(email));
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowValidationExceptionForInvalidPassword() {
        String email = "test@example.com";
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword";

        Customer customer = CustomerTestBuilder.aCustomer()
                .withEmail(Email.with(email))
                .withPassword(encodedPassword)
                .build();

        when(customerRepository.getByEmail(Email.with(email))).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        AuthenticationUseCase.Input input = new AuthenticationUseCase.Input(email, password);

        assertThrows(ValidationException.class, () -> authenticationUseCase.execute(input));
        verify(customerRepository, times(1)).getByEmail(Email.with(email));
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }
}