package com.endava.challenge.newjoiner.profilereader.control.converter;

import org.reactivestreams.Publisher;

public interface ReactiveTypeConverter<TS, TD> {
    Publisher<? extends TD> convert(TS source);
}
