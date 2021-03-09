package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.control.converter.ConversionDescriptor;
import com.endava.challenge.newjoiner.profilereader.control.converter.ReactiveConversionService;
import com.endava.challenge.newjoiner.profilereader.control.converter.ReactiveTypeConverter;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ProjectReactorConversionService implements ReactiveConversionService {
    private final Map<ConversionDescriptor, ReactiveTypeConverter<?,?>> converters;

    public ProjectReactorConversionService(Map<ConversionDescriptor, ReactiveTypeConverter<?, ?>> converters) {
        this.converters = converters;
    }

    @Override
    public ReactiveConversionService subscribe(ConversionDescriptor descriptor, ReactiveTypeConverter<?, ?> converter) {
        this.converters.put(descriptor, converter);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TS, TD> Function<TS, Publisher<? extends TD>> convert(ConversionDescriptor descriptor) {
        return (TS obj) -> Optional.of(descriptor)
                .map(converters::get)
                .map(converter -> (ReactiveTypeConverter<? super TS,? extends TD>) converter)
                .map(converter -> converter.convert(obj))
                .orElseThrow();
    }

    @Override
    public <TS, TD> Function<TS, Publisher<? extends TD>> convert(Class<? extends TS> source, Class<? extends TD> target) {
        return convert(TypeConversionDescriptor.from(source, target));
    }

    @Override
    public <TS, TD> Function<TS, Mono<? extends TD>> convertMono(Class<? extends TS> source, Class<? extends TD> target) {
        return (obj) -> Mono.from(this.<TS, TD>convert(source, target).apply(obj));
    }
}
