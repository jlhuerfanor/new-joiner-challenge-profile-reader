package com.endava.challenge.newjoiner.profilereader.infrastructure.application.reader;

import com.endava.challenge.newjoiner.profilereader.business.reader.ProfileReaderBusiness;
import com.endava.challenge.newjoiner.profilereader.control.converter.ReactiveConversionService;
import com.endava.challenge.newjoiner.profilereader.control.message.MessageQueue;
import com.endava.challenge.newjoiner.profilereader.control.reader.DocsProfileReader;
import com.endava.challenge.newjoiner.profilereader.control.reader.PdfProfileReader;
import com.endava.challenge.newjoiner.profilereader.control.reader.ProfileReadingStrategy;
import com.endava.challenge.newjoiner.profilereader.control.reader.ProfileReadingStrategyMap;
import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProfileReaderConfiguration {

    @Bean
    public ProfileReaderBusiness profileReaderBusiness(ReactiveConversionService converter, MessageQueue messageQueue) {
        return new ProfileReaderBusiness(converter, messageQueue);
    }

    @Bean
    public ProfileReadingStrategy profileReadingStrategy() {
        return new ProfileReadingStrategyMap()
                .registerReader(FileType.PDF, new PdfProfileReader())
                .registerReader(FileType.WORD, new DocsProfileReader())
                .registerReader(FileType.DOCX, new DocsProfileReader());
    }
}
