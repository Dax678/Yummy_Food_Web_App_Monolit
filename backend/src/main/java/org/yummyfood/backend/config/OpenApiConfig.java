package org.yummyfood.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Value("${api.common.title}") String apiTitle;
    @Value("${api.common.version}") String apiVersion;
    @Value("${api.common.description}") String apiDescription;
    @Value("${api.common.contact.name}") String apiContactName;
    @Value("${api.common.contact.email}") String apiContactEmail;
    @Value("${api.common.license.name}") String apiLicenseName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title(apiTitle)
                        .description(apiDescription)
                        .version(apiVersion)
                        .contact(new Contact().name(apiContactName).email(apiContactEmail))
                        .license(new License().name(apiLicenseName))
        );
    }
}
