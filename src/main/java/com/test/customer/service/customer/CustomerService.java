package com.test.customer.service.customer;

import com.test.customer.dto.CustomerDTO;
import com.test.customer.dto.PaginatedResponse;
import com.test.customer.dto.request.RequestCustomerDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CustomerService {

    Optional<CustomerDTO> getById(Long id);

    Optional<CustomerDTO> getByEmail(String email);

    PaginatedResponse<CustomerDTO> getCustomers(int page, int size);

    CustomerDTO create(RequestCustomerDTO customer);

    CustomerDTO update(Long id, RequestCustomerDTO customer);

    void delete(Long id);
}
