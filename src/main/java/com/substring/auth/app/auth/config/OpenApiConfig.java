package com.substring.auth.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Secure Authentication API",
                version = "1.0",
                description = "JWT and OAuth2 based Authentication and Authorization System built using Spring Boot, Spring Security, MySQL, JWT, Google OAuth2 and GitHub OAuth2.",
                contact = @Contact(
                        name = "Rutuja Kavilkar",
                        email = "your-email@gmail.com"
                ),
                license = @License(
                        name = "MIT License"
                )
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}