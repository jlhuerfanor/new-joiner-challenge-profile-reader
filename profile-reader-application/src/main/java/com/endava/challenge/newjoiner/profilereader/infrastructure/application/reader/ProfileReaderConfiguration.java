package com.endava.challenge.newjoiner.profilereader.infrastructure.application.reader;

import com.endava.challenge.newjoiner.profilereader.business.reader.ProfileReaderBusiness;
import com.endava.challenge.newjoiner.profilereader.control.converter.Converter;
import com.endava.challenge.newjoiner.profilereader.control.reader.PdfProfileReader;
import com.endava.challenge.newjoiner.profilereader.control.reader.ProfileReadingStrategy;
import com.endava.challenge.newjoiner.profilereader.control.reader.ProfileReadingStrategyMap;
import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileReaderConfiguration {

    @Bean
    public ProfileReaderBusiness profileReaderBusiness(Converter converter) {
        return new ProfileReaderBusiness(converter);
    }

    @Bean
    public ProfileReadingStrategy profileReadingStrategy() {
        return new ProfileReadingStrategyMap()
                .registerReader(FileType.PDF, new PdfProfileReader());
    }
}
