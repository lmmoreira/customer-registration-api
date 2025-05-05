package com.test.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    final String BASE_URL = "http://api.zippopotam.us";

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(BASE_URL).build();
    }
}
