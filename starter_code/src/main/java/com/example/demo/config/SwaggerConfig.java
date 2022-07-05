package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }
    private ApiInfo apiInfo(){
        return new ApiInfo(
                "eCommercy Api by DuyDN11",
                "API about eCommercy.",
                "1.0",
                "http://www.facebook.com/sngocduy1202",
                new Contact("DuyDN11", "www.udacity.com", "duydang9902@gmail.com"),
                "License of API",
                "http://www.myweb123hihi.com/license",
                Collections.emptyList());
    }
}
