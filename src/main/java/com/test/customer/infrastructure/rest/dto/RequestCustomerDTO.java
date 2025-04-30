package com.test.customer.infrastructure.rest.dto;

import com.test.customer.infrastructure.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestCustomerDTO {
    @NotEmpty(message = "name must not be empty")
    private String name;
    @Email(message = "email should be valid")
    private String email;
    @ValidPhoneNumber(message = "phone should be valid")
    private String phone;
    @NotEmpty(message = "zip code must not be empty")
    private String zipCode;
    @NotEmpty(message = "password code must not be empty")
    private String password;
    @NotEmpty(message = "role must not be empty")
    private String role;
}
