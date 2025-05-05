package com.test.customer.service.customer;

import com.test.customer.domain.CustomerEntity;
import com.test.customer.dto.CustomerDTO;
import com.test.customer.dto.PaginatedResponse;
import com.test.customer.dto.request.RequestCustomerDTO;
import com.test.customer.mapper.CustomerMapper;
import com.test.customer.repository.CustomerRepository;
import com.test.customer.service.exception.CustomerAlreadyExistsException;
import com.test.customer.service.exception.CustomerNotFoundException;
import com.test.customer.service.zipcode.ZipCodeDTO;
import com.test.customer.service.zipcode.ZipCodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ZipCodeService zipCodeService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<CustomerDTO> getById(Long id) {
        return customerRepository.findById(id).map(customerMapper::toDto);
    }

    @Override
    public Optional<CustomerDTO> getByEmail(final String email) {
        return customerRepository.findByEmail(email).map(customerMapper::toDto);
    }

    @Override
    public PaginatedResponse<CustomerDTO> getCustomers(int page, int size) {
        final Page<CustomerEntity> customerPage = customerRepository.findAll(PageRequest.of(page, size));
        return new PaginatedResponse<>(
                customerPage.getContent().stream().map(customerMapper::toDto).toList(),
                customerPage.getNumber(),
                customerPage.getSize(),
                customerPage.getTotalElements()
        );
    }

    @Override
    @Transactional
    public CustomerDTO create(RequestCustomerDTO customer) {

        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already exists");
        }

        final CustomerEntity customerEntity = customerMapper.requestToEntity(customer);
        setEncodedPassword(customerEntity, customer.getPassword());
        setAddressByZipCode(customerEntity, customer.getZipCode());
        return customerMapper.toDto(customerRepository.save(customerEntity));

    }

    @Override
    @Transactional
    public CustomerDTO update(Long id, RequestCustomerDTO customer) {
        final CustomerEntity existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        if (customer.getName() != null) {
            existingCustomer.setName(customer.getName());
        }

        if (customer.getEmail() != null) {
            existingCustomer.setEmail(customer.getEmail());
        }

        if (customer.getPhone() != null) {
            existingCustomer.setTelephone(customer.getPhone());
        }

        if (customer.getPassword() != null) {
            setEncodedPassword(existingCustomer, customer.getPassword());
        }

        if (customer.getZipCode() != null) {
            setAddressByZipCode(existingCustomer, customer.getZipCode());
        }

        return customerMapper.toDto(customerRepository.save(existingCustomer));

    }

    @Override
    @Transactional
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    private void setAddressByZipCode(final CustomerEntity customerEntity, final String zipCode) {
        final ZipCodeDTO zipCodeDTO = zipCodeService.getByZipCode(zipCode).orElse(ZipCodeDTO.builder().postCode(zipCode).build());
        customerEntity.setCountry(zipCodeDTO.getCountry());

        if (zipCodeDTO.getPlaces() != null && zipCodeDTO.getPlaces().getFirst() != null) {
            customerEntity.setPlace(zipCodeDTO.getPlaces().getFirst().getPlaceName());
            customerEntity.setState(zipCodeDTO.getPlaces().getFirst().getState());
        }
    }

    private void setEncodedPassword(final CustomerEntity customerEntity, final String password) {
        customerEntity.setPassword(passwordEncoder.encode(password));
    }

}
