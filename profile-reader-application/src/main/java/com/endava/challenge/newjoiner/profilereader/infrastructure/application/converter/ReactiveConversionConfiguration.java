package com.endava.challenge.newjoiner.profilereader.infrastructure.application.converter;

import com.endava.challenge.newjoiner.profilereader.business.converter.ReactiveConversionBusiness;
import com.endava.challenge.newjoiner.profilereader.control.converter.ConversionDescriptor;
import com.endava.challenge.newjoiner.profilereader.control.converter.ReactiveConversionService;
import com.endava.challenge.newjoiner.profilereader.control.converter.ReactiveTypeConverter;
import com.endava.challenge.newjoiner.profilereader.control.convertion.FilePartToProfileFileConverter;
import com.endava.challenge.newjoiner.profilereader.control.convertion.ProfileFileToProfileConverter;
import com.endava.challenge.newjoiner.profilereader.control.convertion.ProjectReactorConversionService;
import com.endava.challenge.newjoiner.profilereader.control.convertion.TypeConversionDescriptor;
import com.endava.challenge.newjoiner.profilereader.control.reader.ProfileReadingStrategy;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.multipart.FilePart;

import java.util.HashMap;

@Configuration
public class ReactiveConversionConfiguration {

    @Bean
    public ReactiveConversionService reactiveConversionService() {
        return new ProjectReactorConversionService(new HashMap<>());
    }

    @Bean
    public ReactiveConversionBusiness reactiveConversionBusiness(ReactiveConversionService reactiveConversionService) {
        return new ReactiveConversionBusiness(reactiveConversionService);
    }

    @Configuration
    public static class ReactiveConverterConfiguration {
        private final ReactiveConversionService reactiveConversionService;

        @Autowired
        public ReactiveConverterConfiguration(ReactiveConversionService reactiveConversionService) {
            this.reactiveConversionService = reactiveConversionService;
        }

        @Bean
        public FilePartToProfileFileConverter multipartFileToProfileFileConverter() {
            return this.register(
                    TypeConversionDescriptor.from(FilePart.class, ProfileFile.class),
                    new FilePartToProfileFileConverter());
        }

        @Bean
        public ProfileFileToProfileConverter profileFileToProfileConverter(ProfileReadingStrategy profileReadingStrategy) {
            return this.register(
                    TypeConversionDescriptor.from(ProfileFile.class, Profile.class),
                    new ProfileFileToProfileConverter(profileReadingStrategy));
        }

        private <TC extends ReactiveTypeConverter<?,?>> TC register(ConversionDescriptor descriptor, TC instance) {
            this.reactiveConversionService.subscribe(descriptor, instance);
            return instance;
        }
    }
}
