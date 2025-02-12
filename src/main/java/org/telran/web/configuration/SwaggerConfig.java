package org.telran.web.configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Swagger API documentation.
 * Defines API metadata and security configurations for JWT authentication.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures OpenAPI documentation settings.
     *
     * @return OpenAPI instance with API metadata and security configurations.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Diploma Project API")
                        .contact(new Contact().name("Name").email("aaaa@aaaa.com"))
                        .description("API documentation for the shop application.")
                        .version("1.1.2"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
