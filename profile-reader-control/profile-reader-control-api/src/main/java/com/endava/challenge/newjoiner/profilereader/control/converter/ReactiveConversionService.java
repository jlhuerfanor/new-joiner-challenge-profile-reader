package com.endava.challenge.newjoiner.profilereader.control.converter;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface ReactiveConversionService {
    ReactiveConversionService subscribe(ConversionDescriptor descriptor, ReactiveTypeConverter<?,?> converter);

    <TS, TD> Function<TS, Publisher<? extends TD>> convert(ConversionDescriptor descriptor);
    <TS, TD> Function<TS, Publisher<? extends TD>> convert(Class<? extends TS> source, Class<? extends TD> target);

    <TS, TD> Function<TS, Mono<? extends TD>> convertMono(Class<? extends TS> source, Class<? extends TD> target);
}
