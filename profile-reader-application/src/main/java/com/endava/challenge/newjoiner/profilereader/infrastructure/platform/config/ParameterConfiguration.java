package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config;

import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.parameter.CorsParameter;
import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.parameter.LocalizationParameter;
import com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web.security.CorsMapping;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.List;

@Configuration
public class ParameterConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ParameterConfiguration.class);

    @Bean
    public CorsParameter corsParameter(
            @Value("${application.security.cors}") String corsConfig,
            Gson gson) {
        log.debug("Cors configuration: {}", corsConfig);

        return CorsParameter.builder()
                .mappings(gson.fromJson(corsConfig, new TypeToken<List<CorsMapping>>(){}.getType()))
                .build();
    }

    @Bean
    public LocalizationParameter localizationParameter(
            @Value("${application.localization.time.zone-id}") String zoneId,
            @Value("${application.json.format.local-date}") String localDateFormat,
            @Value("${application.json.format.local-time}") String localTimeFormat,
            @Value("${application.json.format.local-datetime}") String localDateTimeFormat) {
        return LocalizationParameter.builder()
                .zoneId(ZoneId.of(zoneId))
                .localDateFormat(localDateFormat)
                .localTimeFormat(localTimeFormat)
                .localDateTimeFormat(localDateTimeFormat)
                .build();
    }
}
