package com.test.customer.infrastructure.rest.dto;

import java.util.List;

public record PaginatedResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements
) {}