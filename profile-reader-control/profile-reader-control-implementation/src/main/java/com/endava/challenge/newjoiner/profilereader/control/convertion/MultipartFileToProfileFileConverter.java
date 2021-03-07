package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class MultipartFileToProfileFileConverter implements Converter<MultipartFile, ProfileFile> {
    private static Logger log = LoggerFactory.getLogger(MultipartFileToProfileFileConverter.class);

    @Override
    public ProfileFile convert(MultipartFile multipartFile) {
        try {
            return ProfileFile.builder()
                    .filename(multipartFile.getOriginalFilename())
                    .fileType(FileType.getByMediaType(multipartFile.getContentType()))
                    .contentStream(multipartFile.getInputStream())
                    .size(multipartFile.getSize())
                    .build();
        } catch (IOException e) {
            log.error("Could not read the file.", e);
            throw new AccessErrorException(e);
        }
    }

    public static class AccessErrorException extends RuntimeException {
        public AccessErrorException(Throwable e) {
            super(e);
        }
    }
}
