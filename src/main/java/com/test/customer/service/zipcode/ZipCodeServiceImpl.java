package com.test.customer.service.zipcode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZipCodeServiceImpl implements ZipCodeService {

    private final WebClient webClient;

    @Override
    public Optional<ZipCodeDTO> getByZipCode(String zipCode) {
        try {
            ZipCodeDTO zip = webClient
                    .get()
                    .uri("/us/{zipCode}", zipCode)
                    .retrieve()
                    .bodyToMono(ZipCodeDTO.class)
                    .timeout(Duration.ofSeconds(2))
                    .retry(3)
                    .block();

            if (zip == null || zip.getPlaces().isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(zip);
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching zip code data", e);
            return Optional.empty();
        }
    }

}
