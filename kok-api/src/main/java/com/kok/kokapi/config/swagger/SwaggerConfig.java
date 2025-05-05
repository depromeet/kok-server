package com.kok.kokapi.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.appname}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl(applicationName);
        return new OpenAPI()
            .info(new Info()
                .title("KOK")
                .version("1.0")
                .description("API 문서입니다."))
            .servers(List.of(server));
    }

    @Bean
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder()
            .group("V1 API")   // Swagger UI에서 "V1 API" 그룹으로 표시
            .pathsToMatch("/v1/api/**")   // v1 관련 API만 포함
            .build();
    }
}

