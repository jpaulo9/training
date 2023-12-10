package com.java.spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class OpenApiConfig {


    @Bean
    OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("RESTFULL API Spring Boot 3.0.6 Java 17")
                        .version("v1")
                        .description("Nerver se Never")
                        .termsOfService("https://license.openai.com")
                        .license(new License().name("Apache 2.0 ").url("https://license.openai.com")));
    }


}
