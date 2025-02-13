package org.telran.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.telran.web.security.model.JwtAuthenticationResponse;
import org.telran.web.security.model.SignInRequest;
import org.telran.web.service.OrdersSchedulerService;

import java.util.logging.Logger;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;
    private static final Logger logger = Logger.getLogger(OrdersSchedulerService.class.getName());


    @Override
    public JwtAuthenticationResponse authenticate(SignInRequest request) {
        logger.info("Аутентификация пользователя");
        authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(request.email(), request.password())
                );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        logger.info("Получения токкена пользователя");
        String token = jwtService.generateToken(userDetails);
        return new JwtAuthenticationResponse(token);
    }
}
