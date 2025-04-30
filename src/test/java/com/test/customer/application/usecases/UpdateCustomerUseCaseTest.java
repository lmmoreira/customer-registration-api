package com.test.customer.application.usecases;

import com.test.customer.application.gateway.ZipCodeGateway;
import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.builders.CustomerTestBuilder;
import com.test.customer.domain.Address;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.domain.exception.NotFoundForUpdateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ZipCodeGateway zipCodeGateway;

    private UpdateCustomerUseCase updateCustomerUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateCustomerUseCase = new UpdateCustomerUseCase(customerRepository, zipCodeGateway);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        Long customerId = 1L;
        String zipCode = "98121";
        Address address = new Address(zipCode, "United States", "Seattle", "Washington");

        Customer existingCustomer = CustomerTestBuilder.aCustomer()
                .withId(customerId)
                .build();

        Customer updatedCustomer = CustomerTestBuilder.aCustomer()
                .withId(customerId)
                .withName("Updated Name")
                .withEmail(Email.with("updated@example.com"))
                .withAddress(address)
                .build();

        when(customerRepository.getById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(zipCodeGateway.getByZipCode(zipCode)).thenReturn(Optional.of(address));
        when(customerRepository.update(any(Customer.class))).thenReturn(updatedCustomer);

        UpdateCustomerUseCase.Input input = new UpdateCustomerUseCase.Input(
                customerId, "Updated Name", "updated@example.com", "+55 (12) 3456789", zipCode, "newPassword", "USER"
        );

        UpdateCustomerUseCase.Output output = updateCustomerUseCase.execute(input);

        assertNotNull(output);
        assertEquals(updatedCustomer.id(), output.id());
        assertEquals(updatedCustomer.name(), output.name());
        assertEquals(updatedCustomer.email().value(), output.email());
        assertEquals(updatedCustomer.address().postCode(), output.postCode());
        assertEquals(updatedCustomer.address().country(), output.country());
        assertEquals(updatedCustomer.address().place(), output.place());
        assertEquals(updatedCustomer.address().state(), output.state());
        verify(customerRepository, times(1)).getById(customerId);
        verify(zipCodeGateway, times(1)).getByZipCode(zipCode);
        verify(customerRepository, times(1)).update(any(Customer.class));
    }

    @Test
    void shouldThrowNotFoundForUpdateExceptionWhenCustomerDoesNotExist() {
        Long customerId = 1L;

        when(customerRepository.getById(customerId)).thenReturn(Optional.empty());

        UpdateCustomerUseCase.Input input = new UpdateCustomerUseCase.Input(
                customerId, "Updated Name", "updated@example.com", "123456789", "98121", "newPassword", "USER"
        );

        assertThrows(NotFoundForUpdateException.class, () -> updateCustomerUseCase.execute(input));
        verify(customerRepository, times(1)).getById(customerId);
        verifyNoInteractions(zipCodeGateway);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void shouldUpdateCustomerWithDefaultAddressWhenZipCodeNotFound() {
        Long customerId = 1L;
        String zipCode = "00000";
        Address defaultAddress = Address.with(zipCode);

        Customer existingCustomer = CustomerTestBuilder.aCustomer()
                .withId(customerId)
                .build();

        Customer updatedCustomer = CustomerTestBuilder.aCustomer()
                .withId(customerId)
                .withAddress(defaultAddress)
                .build();

        when(customerRepository.getById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(zipCodeGateway.getByZipCode(zipCode)).thenReturn(Optional.empty());
        when(customerRepository.update(any(Customer.class))).thenReturn(updatedCustomer);

        UpdateCustomerUseCase.Input input = new UpdateCustomerUseCase.Input(
                customerId, "Updated Name", "updated@example.com", "+55 (12) 3456789", zipCode, "newPassword", "USER"
        );

        UpdateCustomerUseCase.Output output = updateCustomerUseCase.execute(input);

        assertNotNull(output);
        assertEquals(updatedCustomer.id(), output.id());
        assertEquals(updatedCustomer.name(), output.name());
        assertEquals(updatedCustomer.email().value(), output.email());
        assertEquals(updatedCustomer.address().postCode(), output.postCode());
        verify(customerRepository, times(1)).getById(customerId);
        verify(zipCodeGateway, times(1)).getByZipCode(zipCode);
        verify(customerRepository, times(1)).update(any(Customer.class));
    }
}