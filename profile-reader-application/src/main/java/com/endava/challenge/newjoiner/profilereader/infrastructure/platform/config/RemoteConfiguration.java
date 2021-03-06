package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.retry.support.RetryTemplate;

// @Configuration
public class RemoteConfiguration {

    // @Bean("remoteConfigRetryTemplate")
    public static RetryTemplate remoteConfigRetryInterceptor(Environment e) {
        var initialInterval = e.getProperty("config.remote.retry-template.initial-interval", Integer.class, 1000);
        var maxInterval = e.getProperty("config.remote.retry-template.max-interval", Integer.class, 10000);
        var multiplier = e.getProperty("config.remote.retry-template.multiplier", Double.class, 1.6);
        var attempts = e.getProperty("config.remote.retry-template.max-attemps", Integer.class, 5);

        return RetryTemplate.builder()
                .exponentialBackoff(initialInterval, multiplier, maxInterval)
                .maxAttempts(attempts)
                .build();
    }

    // @Configuration
    // @Import({ org.hglteam.config.remote.RemoteConfiguration.class })
    public static class InstanceRemoteConfiguration {
    }
}
