package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.parameter.LocalizationParameter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class JsonConfiguration {
    @Bean
    @Primary
    public Gson gson(
            LocalizationParameter localization) {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
                .registerTypeAdapter(LocalDate.class, new DateTypeAdapters.LocalDateTypeAdapter(localization.getZoneId(), localization.getLocalDateFormat()))
                .registerTypeAdapter(LocalTime.class, new DateTypeAdapters.LocalTimeTypeAdapter(localization.getLocalTimeFormat()))
                .registerTypeAdapter(LocalDateTime.class, new DateTypeAdapters.LocalDateTimeTypeAdapter(localization.getZoneId(), localization.getLocalDateTimeFormat()))
                .create();
    }
}
