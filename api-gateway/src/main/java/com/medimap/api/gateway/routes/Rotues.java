package com.medimap.api.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

import java.net.URI;

@Configuration
public class Rotues {

    @Value("${spring.service.product.url}")
    private String productServiceUrl;

    @Value("${spring.service.order.url}")
    private String orderServiceUrl;

    @Value("${spring.service.inventory.url}")
    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return GatewayRouterFunctions.route("product-service")
                .route(RequestPredicates.path("/api/product"), HandlerFunctions.http(productServiceUrl))
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("product-service", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return GatewayRouterFunctions.route("order-service")
                .route(RequestPredicates.path("/api/order"), HandlerFunctions.http(orderServiceUrl))
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("order-service", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {
        return GatewayRouterFunctions.route("inventory-service")
                .route(RequestPredicates.path("/api/inventory"), HandlerFunctions.http(inventoryServiceUrl))
                .filter(CircuitBreakerFilterFunctions
                        .circuitBreaker("inventory-service", URI.create("forward:/fallbackRoute")))
                .build();
    }



    @Bean
    public RouterFunction<ServerResponse> fallbackRoute(){
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, Please try again later"))
                .build();
    }
}