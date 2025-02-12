package org.telran.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for password encoding.
 * Defines a BCryptPasswordEncoder bean for secure password hashing.
 */
@Configuration
public class PasswordConfig {

    /**
     * Provides a BCryptPasswordEncoder bean.
     *
     * @return PasswordEncoder instance for hashing passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}