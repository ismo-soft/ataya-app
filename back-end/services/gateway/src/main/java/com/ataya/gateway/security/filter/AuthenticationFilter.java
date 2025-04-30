package com.ataya.gateway.security.filter;


import com.ataya.gateway.security.jwt.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilterFactory<AuthenticationFilter.Config>{
    public static class Config {
        // Configuration properties for the filter can be added here
    }
    private final JwtUtils jwtUtils;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Authorization header is missing or invalid");
                return onError(exchange, "Authorization header is missing or invalid", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);
            if (jwtUtils.isTokenExpired(token)) {
                log.warn("JWT token is expired");
                return onError(exchange, "JWT token is expired", HttpStatus.UNAUTHORIZED);
            }

            String username = jwtUtils.extractUsername(token);
            if (username == null || !jwtUtils.getTokenClaims(token).containsKey(username)) {
                log.warn("Invalid JWT token");
                return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
            }
            // check if token contains roles claim
            if (!jwtUtils.getTokenClaims(token).containsKey("roles")) {
                log.warn("JWT token does not contain roles claim");
                return onError(exchange, "JWT token does not contain roles claim", HttpStatus.UNAUTHORIZED);
            }

            log.info("User authenticated: {}", username);
            return chain.filter(exchange);

        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

}
