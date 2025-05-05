package com.test.customer.service.customer;

import com.test.customer.domain.CustomerEntity;
import com.test.customer.dto.CustomerDTO;
import com.test.customer.dto.PaginatedResponse;
import com.test.customer.dto.request.RequestCustomerDTO;
import com.test.customer.mapper.CustomerMapper;
import com.test.customer.repository.CustomerRepository;
import com.test.customer.service.exception.CustomerAlreadyExistsException;
import com.test.customer.service.exception.CustomerNotFoundException;
import com.test.customer.service.zipcode.ZipCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private ZipCodeService zipCodeService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerServiceImpl(customerRepository, customerMapper, zipCodeService, passwordEncoder);
    }

    @Test
    void shouldGetCustomerByIdSuccessfully() {
        Long customerId = 1L;
        CustomerEntity customerEntity = new CustomerEntity();
        CustomerDTO customerDTO = new CustomerDTO();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(customerMapper.toDto(customerEntity)).thenReturn(customerDTO);

        Optional<CustomerDTO> result = customerService.getById(customerId);

        assertTrue(result.isPresent());
        assertEquals(customerDTO, result.get());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerMapper, times(1)).toDto(customerEntity);
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundById() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Optional<CustomerDTO> result = customerService.getById(customerId);

        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(customerId);
        verifyNoInteractions(customerMapper);
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        RequestCustomerDTO request = new RequestCustomerDTO("John Doe", "john@example.com", "+123456789", "12345", "password", "USER");
        CustomerEntity customerEntity = new CustomerEntity();
        CustomerDTO customerDTO = new CustomerDTO();

        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(customerMapper.requestToEntity(request)).thenReturn(customerEntity);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapper.toDto(customerEntity)).thenReturn(customerDTO);

        CustomerDTO result = customerService.create(request);

        assertEquals(customerDTO, result);
        verify(customerRepository, times(1)).findByEmail(request.getEmail());
        verify(customerMapper, times(1)).requestToEntity(request);
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(customerRepository, times(1)).save(customerEntity);
        verify(customerMapper, times(1)).toDto(customerEntity);
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateCustomer() {
        RequestCustomerDTO request = new RequestCustomerDTO("John Doe", "john@example.com", "+123456789", "12345", "password", "USER");

        when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new CustomerEntity()));

        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.create(request));
        verify(customerRepository, times(1)).findByEmail(request.getEmail());
        verifyNoInteractions(customerMapper, passwordEncoder);
    }

    @Test
    void shouldUpdateCustomerSuccessfully() {
        Long customerId = 1L;
        RequestCustomerDTO request = new RequestCustomerDTO("Jane Doe", "jane@example.com", "+987654321", "54321", "newPassword", "USER");
        CustomerEntity existingCustomer = new CustomerEntity();
        CustomerDTO customerDTO = new CustomerDTO();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);
        when(customerMapper.toDto(existingCustomer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.update(customerId, request);

        assertEquals(customerDTO, result);
        verify(customerRepository, times(1)).findById(customerId);
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(customerRepository, times(1)).save(existingCustomer);
        verify(customerMapper, times(1)).toDto(existingCustomer);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentCustomer() {
        Long customerId = 1L;
        RequestCustomerDTO request = new RequestCustomerDTO("Jane Doe", "jane@example.com", "+987654321", "54321", "newPassword", "USER");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.update(customerId, request));
        verify(customerRepository, times(1)).findById(customerId);
        verifyNoInteractions(passwordEncoder, customerMapper);
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        Long customerId = 1L;

        doNothing().when(customerRepository).deleteById(customerId);

        customerService.delete(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void shouldGetPaginatedCustomersSuccessfully() {
        int page = 0;
        int size = 10;
        CustomerEntity customerEntity = new CustomerEntity();
        CustomerDTO customerDTO = new CustomerDTO();
        Page<CustomerEntity> customerPage = new PageImpl<>(List.of(customerEntity));

        when(customerRepository.findAll(PageRequest.of(page, size))).thenReturn(customerPage);
        when(customerMapper.toDto(customerEntity)).thenReturn(customerDTO);

        PaginatedResponse<CustomerDTO> result = customerService.getCustomers(page, size);

        assertEquals(1, result.content().size());
        assertEquals(customerDTO, result.content().get(0));
        verify(customerRepository, times(1)).findAll(PageRequest.of(page, size));
        verify(customerMapper, times(1)).toDto(customerEntity);
    }
}