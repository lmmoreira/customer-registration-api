package com.test.customer.service.auth;

import com.test.customer.dto.LoginDTO;

public interface AuthenticationService {

    LoginDTO login(String email, String password);

}
