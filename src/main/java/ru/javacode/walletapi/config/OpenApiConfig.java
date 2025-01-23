package ru.javacode.walletapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "WALLET-API",
                version = "v1",
                description = "Api for wallet management",
                contact = @Contact(
                        name = "Timofeev Vadim",
                        email = "timofeev.vadim.96@mail.ru",
                        url = "https://t.me/w0nder_waffle"
                )
        )
)
@Configuration
public class OpenApiConfig {
}
