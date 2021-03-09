package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web;

import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.parameter.CorsParameter;
import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web.json.GsonHttpMessageEncoder;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableWebFlux
public class WebFluxConfiguration implements WebFluxConfigurer {
    private final Gson gson;
    private final CorsParameter corsParameter;
    private final ConversionConfigurer conversionConfigurer;

    public WebFluxConfiguration(Gson gson, CorsParameter corsParameter, ConversionConfigurer conversionConfigurer) {
        this.gson = gson;
        this.corsParameter = corsParameter;
        this.conversionConfigurer = conversionConfigurer;
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.customCodecs().register(new GsonHttpMessageEncoder(gson));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        this.conversionConfigurer.getConverters().forEach(registry::addConverter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        for(var mapping : this.corsParameter.getMappings()) {
            registry.addMapping(mapping.getPattern())
                    .allowedOrigins(mapping.getOrigins().toArray(String[]::new))
                    .allowedMethods(mapping.getMethods().toArray(String[]::new))
                    .allowedHeaders(mapping.getHeaders().toArray(String[]::new));
        }
    }

    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler errorWebExceptionHandler() {
        return new GlobalExceptionHandler(gson);
    }

    @Bean
    public WebFilter contextPathWebFilter(@Value("${server.servlet.context-path}") String contextPath) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getURI().getPath().startsWith(contextPath)) {
                return chain.filter(
                        exchange.mutate()
                                .request(request.mutate().contextPath(contextPath).build())
                                .build());
            }
            return chain.filter(exchange);
        };
    }
}
