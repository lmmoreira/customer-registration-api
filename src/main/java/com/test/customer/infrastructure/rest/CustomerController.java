package com.test.customer.infrastructure.rest;

import com.test.customer.application.usecases.*;
import com.test.customer.infrastructure.rest.dto.PaginatedResponse;
import com.test.customer.infrastructure.rest.dto.RequestCustomerDTO;
import com.test.customer.infrastructure.rest.dto.ResponseCustomerDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "customers")
@AllArgsConstructor
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final GetCustomerByEmailUseCase getCustomerByEmailUseCase;
    private final GetAllCustomerUseCase getAllCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ResponseCustomerDTO> create(@Valid @RequestBody RequestCustomerDTO requestCustomerDTO) {
        final var output = createCustomerUseCase.execute(new CreateCustomerUseCase.Input(
                requestCustomerDTO.getName(),
                requestCustomerDTO.getEmail(),
                requestCustomerDTO.getPhone(),
                requestCustomerDTO.getZipCode(),
                passwordEncoder.encode(requestCustomerDTO.getPassword()),
                requestCustomerDTO.getRole()));
        return ResponseEntity.created(URI.create("/customers/" + output.id())).body(new ResponseCustomerDTO(
                output.id(),
                output.name(),
                output.email(),
                output.phone(),
                output.postCode(),
                output.country(),
                output.place(),
                output.state()
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<ResponseCustomerDTO> update(@PathVariable Long id, @Valid @RequestBody RequestCustomerDTO requestCustomerDTO) {
        final var output = updateCustomerUseCase.execute(new UpdateCustomerUseCase.Input(
                id,
                requestCustomerDTO.getName(),
                requestCustomerDTO.getEmail(),
                requestCustomerDTO.getPhone(),
                requestCustomerDTO.getZipCode(),
                passwordEncoder.encode(requestCustomerDTO.getPassword()),
                requestCustomerDTO.getRole()
        ));
        return ResponseEntity.ok().body(new ResponseCustomerDTO(
                output.id(),
                output.name(),
                output.email(),
                output.phone(),
                output.postCode(),
                output.country(),
                output.place(),
                output.state()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCustomerDTO> get(@PathVariable Long id) {
        final var output = getCustomerByIdUseCase.execute(new GetCustomerByIdUseCase.Input(id));
        return ResponseEntity.ok().body(new ResponseCustomerDTO(output.id(),
                output.name(),
                output.email(),
                output.phone(),
                output.postCode(),
                output.country(),
                output.place(),
                output.state()));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<ResponseCustomerDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        final var output = getAllCustomerUseCase.execute(new GetAllCustomerUseCase.Input(page, size));
        final List<ResponseCustomerDTO> response = output.customers().stream()
                .map(customer -> new ResponseCustomerDTO(
                        customer.id(),
                        customer.name(),
                        customer.email(),
                        customer.phone(),
                        customer.postCode(),
                        customer.country(),
                        customer.place(),
                        customer.state()
                ))
                .toList();
        return ResponseEntity.ok(new PaginatedResponse<>(
                response,
                page,
                size,
                output.totalElements()));
    }

    @GetMapping("/email")
    public ResponseEntity<ResponseCustomerDTO> getByEmail(@RequestParam String email) {
        final var output = getCustomerByEmailUseCase.execute(new GetCustomerByEmailUseCase.Input(email));
        return ResponseEntity.ok().body(new ResponseCustomerDTO(
                output.id(),
                output.name(),
                output.email(),
                output.phone(),
                output.postCode(),
                output.country(),
                output.place(),
                output.state()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteCustomerUseCase.execute(new DeleteCustomerUseCase.Input(id));
        return ResponseEntity.noContent().build();
    }

}
