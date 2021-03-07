package com.endava.challenge.newjoiner.profilereader.infrastructure.application.converter;

import com.endava.challenge.newjoiner.profilereader.business.converter.ConversionBusiness;
import com.endava.challenge.newjoiner.profilereader.control.convertion.SpringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ConversionConfiguration {
    private final ConversionService conversionService;
    private Set<Converter<?,?>> converters;

    @Autowired
    public ConversionConfiguration(ConversionService conversionService) {
        this.conversionService = conversionService;
        this.converters = new HashSet<>();
    }

    @Bean
    public SpringConverter springConverter() {
        return new SpringConverter(conversionService);
    }

    @Bean
    public ConversionBusiness conversionBusiness(SpringConverter converter) {
        return new ConversionBusiness(converter);
    }
}
