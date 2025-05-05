package com.test.customer.service.zipcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ZipCodeServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private ZipCodeServiceImpl zipCodeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        zipCodeService = new ZipCodeServiceImpl(webClient);
    }

    @Test
    void shouldReturnZipCodeDataSuccessfully() {
        String zipCode = "12345";
        ZipCodeDTO zipCodeDTO = new ZipCodeDTO();
        zipCodeDTO.setPlaces(List.of(new ZipCodeDTO.Place("PlaceName", "State", "StateAbbreviation")));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/us/{zipCode}", zipCode)).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ZipCodeDTO.class)).thenReturn(Mono.just(zipCodeDTO));

        Optional<ZipCodeDTO> result = zipCodeService.getByZipCode(zipCode);

        assertTrue(result.isPresent());
        assertEquals(zipCodeDTO, result.get());
        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("/us/{zipCode}", zipCode);
        verify(responseSpec, times(1)).bodyToMono(ZipCodeDTO.class);
    }

    @Test
    void shouldReturnEmptyWhenZipCodeNotFound() {
        String zipCode = "12345";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/us/{zipCode}", zipCode)).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ZipCodeDTO.class)).thenReturn(Mono.empty());

        Optional<ZipCodeDTO> result = zipCodeService.getByZipCode(zipCode);

        assertTrue(result.isEmpty());
        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("/us/{zipCode}", zipCode);
        verify(responseSpec, times(1)).bodyToMono(ZipCodeDTO.class);
    }

    @Test
    void shouldReturnEmptyWhenErrorOccurs() {
        String zipCode = "12345";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/us/{zipCode}", zipCode)).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ZipCodeDTO.class)).thenThrow(WebClientResponseException.NotFound.class);

        Optional<ZipCodeDTO> result = zipCodeService.getByZipCode(zipCode);

        assertTrue(result.isEmpty());
        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri("/us/{zipCode}", zipCode);
        verify(responseSpec, times(1)).bodyToMono(ZipCodeDTO.class);
    }
}