package com.example.mensaapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfo("Mensa API - Studentenwerk Würzburg",
                "API für die Mensen & Cafeterien des Studentenwerks Würzburg. KEINE OFFIZIELLE API DES STUDENTENWERKS!",
                "1.0",
                "https://github.com/TimoReusch/studentenwerk-wuerzburg-mensa-api",
                new Contact("GitHub", "https://github.com/TimoReusch/studentenwerk-wuerzburg-mensa-api", ""),
                "",
                "",
                Collections.emptyList());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
