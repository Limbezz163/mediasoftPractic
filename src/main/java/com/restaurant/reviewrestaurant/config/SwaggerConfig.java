package com.restaurant.reviewrestaurant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local server"),
                        new Server().url("https://api.restaurant.com").description("Production server")
                ))
                .info(new Info()
                        .title("Restaurant Review API")
                        .version("1.0")
                        .description("API для управления ресторанами и отзывами")
                        .contact(new Contact()
                                .name("Ваша команда")
                                .email("contact@restaurant.com")
                                .url("https://restaurant.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}