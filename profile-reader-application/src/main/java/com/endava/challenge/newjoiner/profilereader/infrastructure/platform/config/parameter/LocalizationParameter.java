package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.config.parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZoneId;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class LocalizationParameter {
    private ZoneId zoneId;
    private String localDateFormat;
    private String localTimeFormat;
    private String localDateTimeFormat;
}
