package com.endava.challenge.newjoiner.profilereader.infrastructure.application.converter;

import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web.ConversionConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ApplicationConversionConfigurer implements ConversionConfigurer {
    private final Set<Converter<?,?>> converters = new HashSet<>();

    private <T extends Converter<?,?>> T register(T instance) {
        this.converters.add(instance);
        return instance;
    }

    @Override
    public Set<Converter<?, ?>> getConverters() {
        return converters;
    }
}
