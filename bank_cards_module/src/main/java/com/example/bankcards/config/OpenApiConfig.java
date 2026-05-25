package com.example.bankcards.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI bankCardsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Cards API")
                        .version("v1")
                        .description("""
                                REST API for card issuing, customer management, card lifecycle operations,
                                and account balance visibility. The contract follows production API practices:
                                explicit DTO schemas, structured error responses, stable operation IDs, and
                                bearer token authorization metadata.
                                """)
                        .contact(new Contact()
                                .name("Bank Cards Platform Team")
                                .email("platform-team@example.com"))
                        .license(new License()
                                .name("Proprietary")))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Current environment behind API gateway"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development")
                ))
                .tags(List.of(
                        new Tag()
                                .name("Cards")
                                .description("Card issuing, lookup, update, blocking status, and deletion flows."),
                        new Tag()
                                .name("Users")
                                .description("Customer profile and role management.")
                ))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Paste a JWT access token without the Bearer prefix.")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .externalDocs(new ExternalDocumentation()
                        .description("Static OpenAPI contract")
                        .url("/docs/openapi.yaml"));
    }
}

