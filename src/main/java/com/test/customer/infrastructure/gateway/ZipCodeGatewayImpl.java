package com.test.customer.infrastructure.gateway;

import com.test.customer.application.gateway.ZipCodeGateway;
import com.test.customer.domain.Address;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class ZipCodeGatewayImpl implements ZipCodeGateway {

    private final WebClient webClient;

    @Override
    public Optional<Address> getByZipCode(String zipCode) {
        try {
            ZipCodeResponse zip = webClient
                    .get()
                    .uri("/us/{zipCode}", zipCode)
                    .retrieve()
                    .bodyToMono(ZipCodeResponse.class)
                    .timeout(Duration.ofSeconds(2))
                    .retry(3)
                    .block();

            if (zip == null || zip.getPlaces().isEmpty()) {
                return Optional.empty();
            }

            final ZipCodeResponse.Place place = zip.getPlaces().get(0);

            return Optional.of(new Address(
                    zip.getPostCode(),
                    zip.getCountry(),
                    place.getPlaceName(),
                    place.getState()
            ));
        } catch (WebClientResponseException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching zip code data", e);
            return Optional.empty();
        }
    }
}
