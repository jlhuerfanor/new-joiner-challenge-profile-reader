package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web;

import com.endava.challenge.newjoiner.profilereader.control.validation.Validation;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

public class GlobalExceptionHandler implements WebExceptionHandler {
    private static Map<Class<? extends Throwable>, Integer> errorCodeMap = Map.ofEntries(
            Map.entry(Validation.ValidationException.class, 400)
    );

    private final Gson gson;

    public GlobalExceptionHandler(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        var dataBufferFactory = exchange.getResponse().bufferFactory();
        var message = ExceptionMessage.builder()
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .error(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .status(errorCodeMap.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .build();

        exchange.getResponse().setStatusCode(HttpStatus.resolve(message.getStatus()));
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(Mono.just(dataBufferFactory.wrap(gson.toJson(message).getBytes(StandardCharsets.UTF_8))));
    }

    @Getter
    @NoArgsConstructor
    @SuperBuilder(toBuilder = true)
    public static class ExceptionMessage {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
    }
}
