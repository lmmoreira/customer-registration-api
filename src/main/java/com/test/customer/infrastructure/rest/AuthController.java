package com.test.customer.infrastructure.rest;

import com.test.customer.application.usecases.AuthenticationUseCase;
import com.test.customer.infrastructure.rest.dto.LoginRequest;
import com.test.customer.infrastructure.rest.dto.LoginResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationUseCase authenticationUseCase;
    private final JwtEncoder jwtEncoder;


    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        var customer = authenticationUseCase.execute(new AuthenticationUseCase.Input(request.email(), request.password()));

        var now = Instant.now();
        var expiresIn = 7200L;
        var claims = JwtClaimsSet.builder()
                .subject(customer.id().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", customer.role())
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponse(jwtValue, expiresIn);
    }

}
