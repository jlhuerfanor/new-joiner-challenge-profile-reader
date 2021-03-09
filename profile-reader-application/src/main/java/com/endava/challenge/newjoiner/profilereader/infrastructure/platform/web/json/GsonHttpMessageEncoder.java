package com.endava.challenge.newjoiner.profilereader.infrastructure.platform.web.json;

import com.google.gson.Gson;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageEncoder;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GsonHttpMessageEncoder implements HttpMessageEncoder<Object> {
    static final List<MimeType> mimeTypes = Stream.of(
                    new MimeType("application", "json"),
                    new MimeType("application", "*+json"))
            .collect(Collectors.toUnmodifiableList());
    static final Map<MimeType, byte[]> separatorByStreamingMimeType = Map.ofEntries(
            Map.entry(MediaType.APPLICATION_STREAM_JSON, "\n".getBytes(StandardCharsets.US_ASCII)),
            Map.entry(MediaType.parseMediaType("application/stream+x-jackson-smile"), new byte[0]));

    private final Gson gson;

    public GsonHttpMessageEncoder(Gson gson) {
        this.gson = gson;
    }

    @Override
    public List<MediaType> getStreamingMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_STREAM_JSON);
    }

    @Override
    public boolean canEncode(ResolvableType elementType, MimeType mimeType) {
        if(supportsMediaType(mimeType)) {
            var elementClass = elementType.toClass();

            if(Object.class.equals(elementClass)) {
                return true;
            } else if(!String.class.equals(elementType.resolve(elementClass))) {
                return this.isTypeAdapterAvailable(elementClass);
            }
        }
        return false;
    }

    @Override
    public Flux<DataBuffer> encode(Publisher<?> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, MimeType mimeType, Map<String, Object> hints) {
        if(inputStream instanceof Mono) {
            return Mono.from(inputStream)
                    .map(value -> encodeValue(value, bufferFactory, elementType, mimeType, hints))
                    .flux();
        } else {
            if(separatorByStreamingMimeType.containsKey(mimeType)) {
                var separator = separatorByStreamingMimeType.get(mimeType);

                return Flux.from(inputStream)
                        .map(this.encodeWithSeparator(bufferFactory, hints, separator));
            } else {
                var listType = ResolvableType.forClassWithGenerics(List.class, elementType);

                return Flux.from(inputStream)
                        .collectList()
                        .map(list -> encodeValue(list, bufferFactory, listType, mimeType, hints))
                        .flux();
            }
        }
    }

    @Override
    public DataBuffer encodeValue(Object value, DataBufferFactory bufferFactory, ResolvableType valueType, MimeType mimeType, Map<String, Object> hints) {
        return encodeWithSeparator(value, bufferFactory, hints, new byte[0]);
    }

    @Override
    public List<MimeType> getEncodableMimeTypes() {
        return mimeTypes;
    }

    private Function<Object, DataBuffer> encodeWithSeparator(DataBufferFactory bufferFactory, Map<String, Object> hints, byte[] separator) {
        return obj -> encodeWithSeparator(obj, bufferFactory, hints, separator);
    }

    private DataBuffer encodeWithSeparator(Object obj, DataBufferFactory bufferFactory, Map<String, Object> hints, byte[] separator) {
        var bytes = gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
        var buffer = bufferFactory.allocateBuffer(bytes.length + separator.length);

        buffer.write(bytes);
        buffer.write(separator);

        return buffer;
    }

    private boolean isTypeAdapterAvailable(Class<?> elementClass) {
        try {
            gson.getAdapter(elementClass);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean supportsMediaType(MimeType mimeType) {
        return mimeTypes.stream()
                .anyMatch(m -> m.isCompatibleWith(mimeType));
    }
}
