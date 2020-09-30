package com.laffuste.inventorio.interfaces.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig {

    private static final String REDIRECT_URL = "/swagger-ui.html";

    /**
     * Swagger page info
     */
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventorio API")
                        .description("A complete Inventory management solution for your completely fake company")
                        .version("v0.0.1")
                )
                .externalDocs(
                        new ExternalDocumentation()
                        .description("Source code (github)")
                        .url("https://https://github.com/laffuste/inventorio"));
    }

    /**
     * Redirect root to swagger
     */
    @Configuration
    public static class WebConfiguration implements WebMvcConfigurer {
        @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.addRedirectViewController("/", REDIRECT_URL);
        }
    }

}
