package spring.board.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.annotations.OpenAPI31;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@OpenAPI31
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi questionApi() {
        return GroupedOpenApi.builder().group("user-part").pathsToMatch("/question/**", "/api/**").build();
    }

    @Bean
    public GroupedOpenApi answerApi() {
        return GroupedOpenApi.builder().group("admin-part").pathsToMatch("/answer/**", "/api/**").build();
    }

    @Bean
    public GroupedOpenApi loginApi() {
        return GroupedOpenApi.builder().group("login").pathsToMatch("/api/**").build();
    }

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info().title("공지사항 API").version("v1")
                .description("공지사항용 API");

        SecurityScheme bearerAuth = new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name(HttpHeaders.AUTHORIZATION);

        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("JWT");

        return new OpenAPI().components(new Components().addSecuritySchemes("JWT", bearerAuth))
                .addSecurityItem(addSecurityItem).info(info);
    }
}
