package com.test.customer.application.usecases;

import com.test.customer.application.gateway.ZipCodeGateway;
import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.builders.CustomerTestBuilder;
import com.test.customer.domain.Address;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.domain.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ZipCodeGateway zipCodeGateway;

    private CreateCustomerUseCase createCustomerUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createCustomerUseCase = new CreateCustomerUseCase(customerRepository, zipCodeGateway);
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        String email = "test@example.com";
        String zipCode = "98121";
        Address address = new Address(zipCode, "United States", "Seattle", "Washington");

        Customer customer = CustomerTestBuilder.aCustomer()
                .withEmail(Email.with(email))
                .withAddress(address)
                .build();

        when(customerRepository.getByEmail(Email.with(email))).thenReturn(Optional.empty());
        when(zipCodeGateway.getByZipCode(zipCode)).thenReturn(Optional.of(address));
        when(customerRepository.create(any(Customer.class))).thenReturn(customer);

        CreateCustomerUseCase.Input input = new CreateCustomerUseCase.Input(
                customer.name(), email, customer.phone().value(), zipCode, customer.password(), customer.role()
        );

        CreateCustomerUseCase.Output output = createCustomerUseCase.execute(input);

        assertNotNull(output);
        assertEquals(customer.id(), output.id());
        assertEquals(customer.name(), output.name());
        assertEquals(customer.email().value(), output.email());
        assertEquals(customer.address().postCode(), output.postCode());
        verify(customerRepository, times(1)).getByEmail(Email.with(email));
        verify(zipCodeGateway, times(1)).getByZipCode(zipCode);
        verify(customerRepository, times(1)).create(any(Customer.class));
    }

    @Test
    void shouldThrowValidationExceptionWhenCustomerAlreadyExists() {
        String email = "test@example.com";

        Customer existingCustomer = CustomerTestBuilder.aCustomer()
                .withEmail(Email.with(email))
                .build();

        when(customerRepository.getByEmail(Email.with(email))).thenReturn(Optional.of(existingCustomer));

        CreateCustomerUseCase.Input input = new CreateCustomerUseCase.Input(
                existingCustomer.name(), email, existingCustomer.phone().value(), "98121", existingCustomer.password(), existingCustomer.role()
        );

        assertThrows(ValidationException.class, () -> createCustomerUseCase.execute(input));
        verify(customerRepository, times(1)).getByEmail(Email.with(email));
        verifyNoInteractions(zipCodeGateway);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void shouldCreateCustomerWithDefaultAddressWhenZipCodeNotFound() {
        String email = "test@example.com";
        String zipCode = "00000";
        Address defaultAddress = Address.with(zipCode);

        Customer customer = CustomerTestBuilder.aCustomer()
                .withEmail(Email.with(email))
                .withAddress(defaultAddress)
                .build();

        when(customerRepository.getByEmail(Email.with(email))).thenReturn(Optional.empty());
        when(zipCodeGateway.getByZipCode(zipCode)).thenReturn(Optional.empty());
        when(customerRepository.create(any(Customer.class))).thenReturn(customer);

        CreateCustomerUseCase.Input input = new CreateCustomerUseCase.Input(
                customer.name(), email, customer.phone().value(), zipCode, customer.password(), customer.role()
        );

        CreateCustomerUseCase.Output output = createCustomerUseCase.execute(input);

        assertNotNull(output);
        assertEquals(customer.id(), output.id());
        assertEquals(customer.name(), output.name());
        assertEquals(customer.email().value(), output.email());
        assertEquals(customer.address().postCode(), output.postCode());
        verify(customerRepository, times(1)).getByEmail(Email.with(email));
        verify(zipCodeGateway, times(1)).getByZipCode(zipCode);
        verify(customerRepository, times(1)).create(any(Customer.class));
    }
}