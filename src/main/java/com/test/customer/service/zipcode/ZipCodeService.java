package com.test.customer.service.zipcode;

import java.util.Optional;

public interface ZipCodeService {

    Optional<ZipCodeDTO> getByZipCode(String zipCode);

}
