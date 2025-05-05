package com.test.customer.controller;

import com.test.customer.dto.CustomerDTO;
import com.test.customer.dto.request.RequestCustomerDTO;
import com.test.customer.service.customer.CustomerService;
import com.test.customer.service.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        RequestCustomerDTO request = new RequestCustomerDTO("John Doe", "john@example.com", "+123456789", "12345", "password", "USER");
        CustomerDTO expectedResponse = new CustomerDTO(1L, "John Doe", "john@example.com", "+123456789", "12345", "USA", "New York", "NY");

        when(customerService.create(request)).thenReturn(expectedResponse);

        ResponseEntity<CustomerDTO> response = customerController.create(request);

        URI location = URI.create("/customers/" + expectedResponse.getId());
        assertEquals(ResponseEntity.created(location).body(expectedResponse), response);
        verify(customerService, times(1)).create(request);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        Long customerId = 1L;
        RequestCustomerDTO request = new RequestCustomerDTO("Jane Doe", "jane@example.com", "+987654321", "54321", "password", "USER");
        CustomerDTO expectedResponse = new CustomerDTO(1L, "Jane Doe", "jane@example.com", "+987654321", "54321", "USA", "Los Angeles", "CA");

        when(customerService.update(customerId, request)).thenReturn(expectedResponse);

        ResponseEntity<CustomerDTO> response = customerController.update(customerId, request);

        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(customerService, times(1)).update(customerId, request);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundOnUpdate() {
        Long customerId = 1L;
        RequestCustomerDTO request = new RequestCustomerDTO("Jane Doe", "jane@example.com", "+987654321", "54321", "password", "USER");

        when(customerService.update(customerId, request)).thenThrow(new CustomerNotFoundException("Customer not found"));

        assertThrows(CustomerNotFoundException.class, () -> customerController.update(customerId, request));
        verify(customerService, times(1)).update(customerId, request);
    }

    @Test
    void shouldGetCustomerByIdSuccessfully() {
        Long customerId = 1L;
        CustomerDTO expectedResponse = new CustomerDTO(1L, "John Doe", "john@example.com", "+123456789", "12345", "USA", "New York", "NY");

        when(customerService.getById(customerId)).thenReturn(Optional.of(expectedResponse));

        ResponseEntity<CustomerDTO> response = customerController.get(customerId);

        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(customerService, times(1)).getById(customerId);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundOnGetById() {
        Long customerId = 1L;

        when(customerService.getById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerController.get(customerId));
        verify(customerService, times(1)).getById(customerId);
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        Long customerId = 1L;

        ResponseEntity<Void> response = customerController.delete(customerId);

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(customerService, times(1)).delete(customerId);
    }
}