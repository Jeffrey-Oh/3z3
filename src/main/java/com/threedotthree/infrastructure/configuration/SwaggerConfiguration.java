package com.threedotthree.infrastructure.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
            .title("삼쩜삼 API")
            .version("1.0");

        SecurityScheme bearerAuth = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .name(HttpHeaders.AUTHORIZATION);

        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("JWT Token");

        return new OpenAPI()
            .components(new Components().addSecuritySchemes("JWT Token", bearerAuth))
            .addSecurityItem(addSecurityItem)
            .info(info);

    }

}