package com.libreria.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libreriaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Libreria Virtual API")
                        .description("API REST para busqueda, calificacion y resenas de libros")
                        .version("v1"));
    }
}
