package com.test.customer.application.usecases;

import com.test.customer.application.repositories.CustomerRepository;
import com.test.customer.domain.Customer;
import com.test.customer.domain.Email;
import com.test.customer.domain.exception.CustomerNotFoundException;
import com.test.customer.domain.exception.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationUseCase implements UseCase<AuthenticationUseCase.Input, AuthenticationUseCase.Output> {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationUseCase(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthenticationUseCase.Output execute(AuthenticationUseCase.Input input) {
        var customer = customerRepository.getByEmail(Email.with(input.email()))
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        validatePassword(input.password, customer.password());

        return AuthenticationUseCase.Output.of(customer);
    }

    public record Input(String email, String password) {
    }

    public record Output(Long id, String name, String email, String phone, String postCode, String country,
                         String place, String state, String role) {
        static AuthenticationUseCase.Output of(Customer customer) {
            return new AuthenticationUseCase.Output(customer.id(), customer.name(), customer.email().value(), customer.phone().value(),
                    customer.address().postCode(), customer.address().country(), customer.address().place(), customer.address().state(), customer.role());
        }
    }

    private void validatePassword(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new ValidationException("E-mail or password is incorrect");
        }
    }
}
