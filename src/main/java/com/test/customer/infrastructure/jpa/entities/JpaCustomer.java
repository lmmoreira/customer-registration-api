package com.test.customer.infrastructure.jpa.entities;

import com.test.customer.domain.Address;
import com.test.customer.domain.Customer;
import com.test.customer.infrastructure.validation.ValidPhoneNumber;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
public class JpaCustomer {

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

    public Customer toCustomer() {
        return Customer.createCustomer(this.id, this.name, this.email, this.telephone, new Address(this.postCode, this.country, this.place, this.state), this.password, this.role);
    }

    public static JpaCustomer of(final Customer customer) {
        return new JpaCustomer(customer.id(), customer.name(), customer.email().value(), customer.phone().value(),
                customer.address().postCode(), customer.address().country(), customer.address().place(), customer.address().state(), customer.password(), customer.role());
    }

}
