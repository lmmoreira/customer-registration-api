package com.test.customer.controller;

import com.test.customer.dto.CustomerDTO;
import com.test.customer.dto.PaginatedResponse;
import com.test.customer.dto.request.RequestCustomerDTO;
import com.test.customer.service.customer.CustomerService;
import com.test.customer.service.exception.CustomerNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody RequestCustomerDTO requestCustomerDTO) {
        final var response = customerService.create(requestCustomerDTO);
        return ResponseEntity.created(URI.create("/customers/" + response.getId())).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @Valid @RequestBody RequestCustomerDTO requestCustomerDTO) {
        final var response = customerService.update(id, requestCustomerDTO);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> get(@PathVariable Long id) {
        final var response = customerService.getById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<CustomerDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var pageable = customerService.getCustomers(page, size);
        return ResponseEntity.ok(pageable);
    }

    @GetMapping("/email")
    public ResponseEntity<CustomerDTO> getByEmail(@RequestParam String email) {
        final var response = customerService.getByEmail(email).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
