package com.xiaoslab.coffee.api.beans;

import com.xiaoslab.coffee.api.utility.UserUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class UtilityBeans {

    @Bean
    public UserUtility userUtility() {
        return new UserUtility();
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xiaoslab.coffee.api.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Xipli REST API",
                "",
                "1.0",
                "",
                new Contact("Xiaoslab Inc.", "www.xiaoslab.com", "info@xiaoslab.com"),
                "© All Rights Reserved by Xiaoslab Inc.",
                "");
    }
}
