package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.control.converter.ReactiveTypeConverter;
import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.io.*;
import java.util.function.Supplier;

public class FilePartToProfileFileConverter implements ReactiveTypeConverter<FilePart, ProfileFile> {
    private static Logger log = LoggerFactory.getLogger(FilePartToProfileFileConverter.class);

    @Override
    public Mono<ProfileFile> convert(FilePart multipartFile) {
        try {
            var temporalFile = File.createTempFile(multipartFile.filename(), null);

            return Mono.just(temporalFile)
                    .flatMap(multipartFile::transferTo)
                    .then(Mono.fromSupplier(this.createInputStream(temporalFile)))
                    .map(fileStream -> ProfileFile.builder()
                            .filename(multipartFile.filename())
                            .fileType(FileType.getByMediaType(multipartFile.headers().getContentType().toString()))
                            .contentStream(fileStream)
                            .size(temporalFile.length())
                            .build());
        } catch (Exception e) {
            log.error("Could not read the file.", e);
            throw new IllegalStateException(e);
        }
    }

    private Supplier<InputStream> createInputStream(File temporalFile) {
        return () -> {
            try {
                return new FileInputStream(temporalFile);
            } catch (FileNotFoundException e) {
                throw new IllegalStateException(e);
            }
        };
    }
}
