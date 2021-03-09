package com.endava.challenge.newjoiner.profilereader.business.converter;

import com.endava.challenge.newjoiner.profilereader.control.converter.ConversionDescriptor;
import com.endava.challenge.newjoiner.profilereader.control.converter.ReactiveConversionService;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ReactiveConversionBusiness {
    private final ReactiveConversionService reactiveConversionService;

    public ReactiveConversionBusiness(ReactiveConversionService reactiveConversionService) {
        this.reactiveConversionService = reactiveConversionService;
    }

    public <TS, TD> Function<TS, Publisher<? extends TD>> convert(ConversionDescriptor descriptor) {
        return this.reactiveConversionService.convert(descriptor);
    }

    public <TS, TD> Function<TS, Publisher<? extends TD>> convert(Class<? extends TS> source, Class<? extends TD> target) {
        return this.reactiveConversionService.convert(source, target);
    }

    public <TS, TD> Function<TS, Mono<? extends TD>> convertMono(Class<? extends TS> source, Class<? extends TD> target) {
        return this.reactiveConversionService.convertMono(source, target);
    }
}
