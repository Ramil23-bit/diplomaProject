//package org.telran.web.configuration;
//
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.telran.web.security.AuthenticationService;
//import org.telran.web.security.AuthenticationServiceImpl;
//import org.telran.web.security.JwtService;
//
///**
// * This class provides a security configuration specifically for testing purposes.
// * It defines authentication, authorization, password encoding, and JWT services for securing API endpoints.
// *
// *   Key Features:
// * - Uses `@Profile("test")` to ensure this configuration is **only active in the test environment**.
// * - Configures Spring Security settings, including authentication and access control.
// * - Implements JWT token management for user authentication.
// * - Defines role-based access control for API endpoints.
// */
//
//@Configuration
//@Profile("test")
//public class TestSecurityConfig {
//
//    /**
//     *   Password Encoder
//     * - Uses BCrypt to securely hash passwords.
//     * - Ensures that user passwords are stored securely.
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     *   Authentication Service
//     * - Provides authentication logic for users.
//     * - Uses `AuthenticationServiceImpl` to manage user login and token handling.
//     */
//    @Bean
//    public AuthenticationService authenticationService() {
//        return new AuthenticationServiceImpl();
//    }
//
//    /**
//     *   Authentication Manager
//     * - Manages authentication requests.
//     * - Used to **validate credentials** during login.
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        return authenticationManagerBuilder.build();
//    }
//
//    @Value("${jwttoken.signing.key}")
//    private String jwtSigningKey;
//
//    /**
//     *   JWT Service
//     * - Handles **JWT token generation and validation**.
//     * - Uses the signing key from application properties.
//     */
//    @Bean
//    public JwtService jwtService() {
//        return new JwtService(jwtSigningKey);
//    }
//
//    /**
//     *   Security Filter Chain
//     * - Configures **role-based access control.
//     * - Defines which endpoints are protected and which are public.
//     * - Disables CSRF protection for testing purposes.
//     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeRequests(auth -> auth
//                        // Order Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/orders").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/orders").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/orders").hasAnyRole("USER", "ADMIN")
//
//                        // Category Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/categories").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/categories").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/categories").hasAnyRole("USER", "ADMIN")
//
//                        // Favorites Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/favorites").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/favorites").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/favorites").hasAnyRole("USER", "ADMIN")
//
//                        // Payment Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/payment").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/payment").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/payment").hasAnyRole("USER", "ADMIN")
//
//                        // Product Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/products").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/products").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products").hasAnyRole("USER", "ADMIN")
//
//                        // Cart Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/cart").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/cart").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/cart").hasAnyRole("USER", "ADMIN")
//
//                        // Cart Items Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/cart_items/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/cart_items/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/cart_items/**").hasAnyRole("USER", "ADMIN")
//
//                        // Order Items Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/order-items").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/order-items").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/order-items").hasAnyRole("USER", "ADMIN")
//
//                        // Storage Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/storage").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/storage").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/storage").hasAnyRole("USER", "ADMIN")
//
//                        // User Management
//                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAnyRole("USER", "ADMIN")
//
//                        // Allow public access to all other requests
//                        .anyRequest().permitAll())
//                .exceptionHandling(config -> config.authenticationEntryPoint(
//                        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
//                ));
//        return http.build();
//    }
//}