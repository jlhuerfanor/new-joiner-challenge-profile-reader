package com.endava.challenge.newjoiner.profilereader.control.converter;

import java.util.function.Function;

public interface Converter {
    <TR> TR convert(Object object, Class<TR> targetClass);

    <TS, TR> TR convert(TS object, Class<? super TS> sourceClass, Class<TR> targetClass);

    <TS, TR> Function<? super TS, ? extends TR> convertTo(Class<TR> targetClass);

    <TR, TS> Function<? super TS, TR> convertTo(Class<? super TS> sourceClass, Class<TR> targetClass);
}
