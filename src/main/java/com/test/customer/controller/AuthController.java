package com.test.customer.controller;

import com.test.customer.dto.LoginDTO;
import com.test.customer.dto.request.LoginRequest;
import com.test.customer.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid LoginRequest request) {
        var response = authenticationService.login(request.email(), request.password());
        return ResponseEntity.ok().body(response);
    }

}
