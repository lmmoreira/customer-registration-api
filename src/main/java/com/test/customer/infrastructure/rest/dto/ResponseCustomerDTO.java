package com.test.customer.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseCustomerDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String postCode;
    private String country;
    private String place;
    private String state;
}
