package com.studio.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Documentação Studio Many",
                version = "1.0",
                description = "GRUPO 5<br>" +
                        "Bill Hebert<br>" +
                        "Davi Santana<br>" +
                        "Dereck Murillo<br>" +
                        "Guilherme Aoki<br>" +
                        "João Pedro Pinheiro<br>" +
                        "Rafael Mechi<br>"
        )
)

public class OpenApiConfig {
}