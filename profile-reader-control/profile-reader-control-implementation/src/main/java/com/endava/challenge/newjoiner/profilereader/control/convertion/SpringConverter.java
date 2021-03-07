package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.control.converter.Converter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.util.function.Function;

public class SpringConverter implements Converter {
    private final ConversionService conversionService;

    public SpringConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public <TR> TR convert(Object object, Class<TR> targetClass) {
        return conversionService.convert(object, targetClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TS, TR> TR convert(TS object, Class<? super TS> sourceClass, Class<TR> targetClass) {
        return (TR) conversionService.convert(object, TypeDescriptor.valueOf(sourceClass), TypeDescriptor.valueOf(targetClass));
    }

    @Override
    public <TS, TR> Function<? super TS, TR> convertTo(Class<TR> targetClass) {
        return obj -> this.convert(obj, targetClass);
    }

    @Override
    public <TR, TS> Function<? super TS, TR> convertTo(Class<? super TS> sourceClass, Class<TR> targetClass) {
        return obj -> this.convert(obj, sourceClass, targetClass);
    }
}
