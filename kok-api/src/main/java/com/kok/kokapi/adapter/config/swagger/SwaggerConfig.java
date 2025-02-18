package com.kok.kokapi.adapter.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KOK")
                        .version("1.0")
                        .description("API 문서입니다.")
                        .contact(new Contact().name("디프만16기 4조").url("https://www.depromeet.com/")));
    }

    @Bean
    public GroupedOpenApi kokApi() {
        return GroupedOpenApi.builder()
                .group("KOK api")
                .pathsToMatch("/v1/api/**")
                .build();
    }
}

