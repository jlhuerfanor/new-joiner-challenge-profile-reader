package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web;

import org.springframework.core.convert.converter.Converter;

import java.util.Set;

public interface ConversionConfigurer {
    Set<Converter<?,?>> getConverters();
}
