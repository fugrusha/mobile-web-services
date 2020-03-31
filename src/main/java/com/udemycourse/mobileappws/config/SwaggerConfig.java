package com.udemycourse.mobileappws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    Contact contact = new Contact(
            "Andrew Golovko",
            "http://localhost",
            "test@gmail.com");

    ApiInfo apiInfo = new ApiInfo(
            "Simple REST web service",
            "This page documents REST web service endpoints",
            "http://localhost",
            "1.0",
            contact,
            "Apache 2.0",
            "http://localhost",
            new ArrayList<>());

    @Bean
    public Docket apiDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .protocols(Set.of("HTTP", "HTTPS"))
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.udemycourse.mobileappws"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

    @Bean // added to solve that issue https://stackoverflow.com/questions/58431876/spring-boot-2-2-0-spring-hateoas-startup-issue
    public LinkDiscoverers discovers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }

}
