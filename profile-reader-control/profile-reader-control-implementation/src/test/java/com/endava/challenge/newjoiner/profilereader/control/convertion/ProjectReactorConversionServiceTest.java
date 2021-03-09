package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.control.converter.ReactiveTypeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ProjectReactorConversionServiceTest {

    private ProjectReactorConversionService service;
    private Integer valueToConvert;
    private Mono<BigInteger> result;

    @BeforeEach
    void setupTest() {
        service = new ProjectReactorConversionService(new HashMap<>());
    }

    @Test
    void convertSuccess() {
        givenATypeConverterRegistered();
        givenAValueToConvert();

        whenIConvertTheValue();
        thenIGetTheExpectedResult();
    }

    private void givenAValueToConvert() {
        this.valueToConvert = 23;
    }

    private void whenIConvertTheValue() {
        this.result = Mono.just(this.valueToConvert)
                .flatMap(this.service.convertMono(Number.class, BigInteger.class));
    }

    private void thenIGetTheExpectedResult() {
        var verifier = StepVerifier.create(this.result);

        verifier.expectNext(BigInteger.valueOf(this.valueToConvert.longValue()));
        verifier.verifyComplete();
    }

    private void givenATypeConverterRegistered() {
        service.subscribe(TypeConversionDescriptor.from(Number.class, BigInteger.class), new NumberToBigDecimalConverter());
    }

    private static class NumberToBigDecimalConverter implements ReactiveTypeConverter<Number, BigInteger> {
        @Override
        public Publisher<? extends BigInteger> convert(Number source) {
            return Mono.just(BigInteger.valueOf(source.longValue()));
        }
    }
}