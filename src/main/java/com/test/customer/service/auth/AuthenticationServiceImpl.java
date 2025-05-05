package com.test.customer.service.auth;

import com.test.customer.dto.LoginDTO;
import com.test.customer.repository.CustomerRepository;
import com.test.customer.service.exception.AuthException;
import com.test.customer.service.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Override
    public LoginDTO login(String email, String password) {
        var customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        validatePassword(password, customer.getPassword());

        var now = Instant.now();
        var expiresIn = 7200L;
        var claims = JwtClaimsSet.builder()
                .subject(customer.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", customer.getRole())
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginDTO(jwtValue, expiresIn);
    }

    private void validatePassword(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new AuthException("E-mail or password is incorrect");
        }
    }

}
