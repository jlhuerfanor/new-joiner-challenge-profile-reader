package com.endava.challenge.newjoiner.profilereader.infrastructure.application.converter;

import com.endava.challenge.newjoiner.profilereader.control.convertion.MultipartFileToProfileFileConverter;
import com.endava.challenge.newjoiner.profilereader.control.convertion.ProfileFileToProfileConverter;
import com.endava.challenge.newjoiner.profilereader.control.reader.ProfileReadingStrategy;
import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web.ConversionConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class ApplicationConversionConfigurer implements ConversionConfigurer {
    private final Set<Converter<?,?>> converters = new HashSet<>();

    @Autowired
    private ProfileReadingStrategy profileReadingStrategy;

    private <T extends Converter<?,?>> T register(T instance) {
        this.converters.add(instance);
        return instance;
    }

    @Override
    public Set<Converter<?, ?>> getConverters() {
        if(converters.isEmpty()) {
            this.register(new ProfileFileToProfileConverter(profileReadingStrategy));
            this.register(new MultipartFileToProfileFileConverter());
        }
        return converters;
    }
}
