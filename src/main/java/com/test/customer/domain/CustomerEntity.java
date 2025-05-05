package com.test.customer.domain;

import com.test.customer.validation.ValidPhoneNumber;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "name must not be empty")
    private String name;
    @Email(message = "email should be valid")
    private String email;
    @ValidPhoneNumber(message = "phone should be valid")
    private String telephone;
    @NotEmpty(message = "name must not be empty")
    private String postCode;
    private String country;
    private String place;
    private String state;
    private String password;
    private String role;
}
