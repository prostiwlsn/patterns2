package ru.hits.core.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApiWithEmptyServer() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI().addServersItem(new Server().url(""))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer addSecurityToProtectedEndpoints() {
        return openApi -> {
            Paths paths = openApi.getPaths();
            final String securitySchemeName = "bearerAuth";

            for (Map.Entry<String, PathItem> entry : paths.entrySet()) {
                PathItem pathItem = entry.getValue();

                applySecurityToOperations(pathItem, securitySchemeName);
            }
        };
    }

    private void applySecurityToOperations(PathItem pathItem, String securitySchemeName) {
        for (Operation operation : pathItem.readOperations()) {
            operation.addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
        }
    }
}
