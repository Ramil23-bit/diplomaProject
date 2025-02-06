package org.telran.web.security;

import org.telran.web.security.model.JwtAuthenticationResponse;
import org.telran.web.security.model.SignInRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse authenticate(SignInRequest request);
}
