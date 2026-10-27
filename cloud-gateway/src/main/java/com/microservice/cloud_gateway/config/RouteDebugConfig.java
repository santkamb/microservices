package com.microservice.cloud_gateway.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteDebugConfig {

    @Bean
    public ApplicationRunner routes(RouteLocator routeLocator) {
        return args -> routeLocator.getRoutes()
                .subscribe(route ->
                        System.out.println(
                                "Loaded Route : "
                                        + route.getId()));
    }
}