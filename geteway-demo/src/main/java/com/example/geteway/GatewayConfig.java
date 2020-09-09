package com.example.geteway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Map;

@Configuration
public class GatewayConfig {


    @Bean
    public GlobalFilter customGlobalFilter() {


        return (exchange, chain) -> exchange.getPrincipal()
                .map(Principal::getName)
                .defaultIfEmpty("Default User")
                .map(userName -> {
                    //adds header to proxied request
                    exchange.getRequest().getHeaders().get("access-token");

                    exchange.getRequest().mutate()
                            .header("user-info", userName)
                            .build();
                    return exchange;
                }).flatMap(chain::filter);
    }

    @Bean
    public GlobalFilter customGlobalPostFilter() {

        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.just(exchange))
                .map(serverWebExchange -> {
                    //adds header to response
                    serverWebExchange.getResponse().getHeaders().set("custom-response-header",
                            HttpStatus.OK.equals(serverWebExchange.getResponse().getStatusCode()) ? "It worked" : "It did not work");
                    System.out.println(" = =====> over ");
                    return serverWebExchange;
                }).then();
    }
}