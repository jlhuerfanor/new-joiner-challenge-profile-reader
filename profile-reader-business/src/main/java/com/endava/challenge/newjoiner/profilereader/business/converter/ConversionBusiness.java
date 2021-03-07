package com.endava.challenge.newjoiner.profilereader.business.converter;

import com.endava.challenge.newjoiner.profilereader.control.converter.Converter;

import java.util.function.Function;

public class ConversionBusiness {
    private final Converter converter;

    public ConversionBusiness(Converter converter) {
        this.converter = converter;
    }

    public <TR> TR convert(Object object, Class<TR> targetClass) {
        return converter.convert(object, targetClass);
    }

    public <TS, TR> TR convert(TS object, Class<? super TS> sourceClass, Class<TR> targetClass) {
        return converter.convert(object, sourceClass, targetClass);
    }

    public <TS, TR> Function<? super TS, ? extends TR> convertTo(Class<TR> targetClass) {
        return converter.convertTo(targetClass);
    }

    public <TS, TR> Function<? super TS, TR> convertTo(Class<? super TS> sourceClass, Class<TR> targetClass) {
        return converter.convertTo(sourceClass, targetClass);
    }
}
