package com.test.customer.application.gateway;

import com.test.customer.domain.Address;

import java.util.Optional;

public interface ZipCodeGateway {

    Optional<Address> getByZipCode(String zipCode);

}