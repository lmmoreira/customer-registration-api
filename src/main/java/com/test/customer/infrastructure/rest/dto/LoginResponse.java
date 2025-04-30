package com.test.customer.infrastructure.rest.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}