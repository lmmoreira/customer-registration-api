package com.test.customer;

import com.test.customer.infrastructure.rest.dto.LoginResponse;
import com.test.customer.infrastructure.rest.dto.RequestCustomerDTO;
import com.test.customer.infrastructure.rest.dto.ResponseCustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthCreateCustomerAndGetIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private String bearerToken;

    @BeforeEach
    void setUp() {
        String loginPayload = """
                {
                    "email": "johndoe@example.com",
                    "password": "password"
                }
                """;

        bearerToken = webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginPayload)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .returnResult()
                .getResponseBody().accessToken();
    }

    @Test
    void shouldCreateAndRetrieveCustomer() {
        RequestCustomerDTO requestCustomerDTO = new RequestCustomerDTO(
                "Jane Doe",
                "janedoe@example.com",
                "+55 (12) 982553850",
                "98121",
                "password",
                "USER"
        );

        ResponseCustomerDTO createdCustomer = webTestClient.post()
                .uri("/customers")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestCustomerDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResponseCustomerDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(createdCustomer);
        assertNotNull(createdCustomer.getId());
        assertEquals("Jane Doe", createdCustomer.getName());
        assertEquals("janedoe@example.com", createdCustomer.getEmail());

        ResponseCustomerDTO retrievedCustomer = webTestClient.get()
                .uri("/customers/" + createdCustomer.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ResponseCustomerDTO.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(retrievedCustomer);
        assertEquals(createdCustomer.getId(), retrievedCustomer.getId());
        assertEquals(createdCustomer.getName(), retrievedCustomer.getName());
        assertEquals(createdCustomer.getEmail(), retrievedCustomer.getEmail());
    }
}