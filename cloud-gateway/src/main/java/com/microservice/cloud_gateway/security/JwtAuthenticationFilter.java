package com.microservice.cloud_gateway.security;

import com.microservice.cloud_gateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class JwtAuthenticationFilter
        implements GlobalFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        System.out.println("JwtAuthenticationFilter Bean Created");

    }

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {
        System.out.println("JWT Filter Executed");
        String path =
                exchange.getRequest()
                        .getURI()
                        .getPath();

        if(path.startsWith("/api/auth")) {
            return chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(
                                HttpHeaders.AUTHORIZATION);

        if(authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED);

            return exchange.getResponse()
                    .setComplete();
        }

        String token =
                authHeader.substring(7);

        if(!jwtUtil.validateToken(token)) {

            exchange.getResponse()
                    .setStatusCode(
                            HttpStatus.UNAUTHORIZED);

            return exchange.getResponse()
                    .setComplete();
        }

        String username =
                jwtUtil.extractUsername(token);

        ServerHttpRequest request =
                exchange.getRequest()
                        .mutate()
                        .header(
                                "X-Authenticated-User",
                                username)
                        .build();

        return chain.filter(
                exchange.mutate()
                        .request(request)
                        .build());
    }
}