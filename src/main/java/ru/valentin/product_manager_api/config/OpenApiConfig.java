package ru.valentin.product_manager_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Product-manager API",
                version = "1.0",
                description = "API для управления товарами с синхронизацией из внешнего сервиса"
        )
)
public class OpenApiConfig {
}