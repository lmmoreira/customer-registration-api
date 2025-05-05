package com.test.customer.service.auth;

import com.test.customer.domain.CustomerEntity;
import com.test.customer.dto.LoginDTO;
import com.test.customer.repository.CustomerRepository;
import com.test.customer.service.exception.AuthException;
import com.test.customer.service.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthenticationServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationServiceImpl(customerRepository, passwordEncoder, jwtEncoder);
    }

    @Test
    void shouldLoginSuccessfully() {
        String email = "test@example.com";
        String password = "password";
        String encodedPassword = "encodedPassword";
        String jwtToken = "jwtToken";
        Long customerId = 1L;
        String role = "USER";

        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        customer.setEmail(email);
        customer.setPassword(encodedPassword);
        customer.setRole(role);

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(customerId.toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(7200))
                .claim("scope", role)
                .build();

        Jwt jwtResult = mock(Jwt.class);
        when(jwtResult.getTokenValue()).thenReturn(jwtToken);

        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwtResult);

        LoginDTO result = authenticationService.login(email, password);

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(jwtEncoder, times(1)).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {
        String email = "nonexistent@example.com";
        String password = "password";

        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> authenticationService.login(email, password));
        verify(customerRepository, times(1)).findByEmail(email);
        verifyNoInteractions(passwordEncoder, jwtEncoder);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        String email = "test@example.com";
        String password = "wrongPassword";
        String encodedPassword = "encodedPassword";

        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setEmail(email);
        customer.setPassword(encodedPassword);

        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        assertThrows(AuthException.class, () -> authenticationService.login(email, password));
        verify(customerRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verifyNoInteractions(jwtEncoder);
    }
}