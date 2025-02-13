package org.telran.web.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.telran.web.security.JwtAuthenticationFilter;

@Configuration
@Profile("test")
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/cart/current").authenticated() // Require authentication
                        .requestMatchers(HttpMethod.POST, "/api/v1/cart").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/cart").hasRole("ADMIN") // Только ADMIN может смотреть все корзины
                        .requestMatchers(HttpMethod.POST, "/api/v1/cart_items/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/order-items").authenticated() // Require authentication
                        .anyRequest().authenticated()) // Other requests require authentication
                .exceptionHandling(config -> config.authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                ));
        return http.build();
    }
}
