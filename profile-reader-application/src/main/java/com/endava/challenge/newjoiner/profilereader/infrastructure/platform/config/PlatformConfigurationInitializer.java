package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config;

import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.YAMLPropertySourceFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlatformConfigurationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private Map<String, EncodedResource> getPropertySources() {
        return Stream.of(
                Map.entry("bootstrap", new ClassPathResource("bootstrap.yml")),
                Map.entry("application", new ClassPathResource("application.yml")))
                .map(entry -> Map.entry(entry.getKey(), new EncodedResource(entry.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        var propertySources = applicationContext.getEnvironment()
                .getPropertySources();
        var factory = new YAMLPropertySourceFactory();

        getPropertySources().entrySet().stream()
                .map(this.toPropertySource(factory))
                .forEach(propertySources::addLast);
    }

    private Function<Map.Entry<String, EncodedResource>, PropertySource<?>> toPropertySource(PropertySourceFactory factory) {
        return entry -> {
            try {
                return factory.createPropertySource(entry.getKey(), entry.getValue());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }
}
