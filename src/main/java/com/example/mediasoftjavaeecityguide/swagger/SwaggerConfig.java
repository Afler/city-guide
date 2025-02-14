package com.example.mediasoftjavaeecityguide.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Value("${app.version}")
    private String version;

    @Value("${app.description}")
    private String description;

    /**
     * Заголовок, версия, и описание приложения на странице сваггера
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Городской гид")
                .version(version)
                .description(description));
    }
}
