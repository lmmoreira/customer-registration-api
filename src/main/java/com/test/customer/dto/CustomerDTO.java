package com.test.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String postCode;
    private String country;
    private String place;
    private String state;
}
