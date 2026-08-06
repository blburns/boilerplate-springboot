package com.boilerplate.springboot.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  OpenAPI boilerplateOpenApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Boilerplate Spring Boot API")
                .description("Cloneable multi-module Spring Boot application base")
                .version("0.1.0"));
  }
}
