package com.substring.auth.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Secure Authentication and Authorization API",
                version = "v1",
                description = "Production-ready authentication system implementing JWT, Refresh Tokens, OAuth2 (Google and GitHub), Role-Based Access Control and REST APIs using Spring Boot and MySQL.",
                contact = @Contact(
                        name = "Rutuja Kavilkar",
                        email = "rutujabkavilkar19@gmail.com"
                )
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class APIDocConfig {

}
