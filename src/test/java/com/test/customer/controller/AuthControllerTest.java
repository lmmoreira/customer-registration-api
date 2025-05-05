package com.test.customer.controller;

import com.test.customer.dto.LoginDTO;
import com.test.customer.dto.request.LoginRequest;
import com.test.customer.service.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnLoginResponseWhenValidRequest() {
        LoginRequest loginRequest = new LoginRequest("johndoe@example.com", "password");
        LoginDTO expectedResponse = new LoginDTO("accessToken123", 1200L);

        when(authenticationService.login(loginRequest.email(), loginRequest.password()))
                .thenReturn(expectedResponse);

        ResponseEntity<LoginDTO> response = authController.login(loginRequest);

        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(authenticationService, times(1)).login(loginRequest.email(), loginRequest.password());
    }
}