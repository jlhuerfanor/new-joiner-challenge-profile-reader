package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class MultipartFileToProfileFileConverterTest {

    private MultipartFileToProfileFileConverter converter;
    private MockedMultipartFile multipartFile;
    private ProfileFile profileFileObtained;

    @BeforeEach
    void setupTest() {
        converter = new MultipartFileToProfileFileConverter();
    }

    @Test
    void convertSuccess() throws IOException {
        givenAPdfMultipartFile();
        whenIConvertTheFileToProfileFile();
        thenIGetTheExpectedProfileFile();
    }

    @Test
    void conversionSuccessWithNullFileType() throws IOException {
        givenAPdfMultipartFileWithAnUnknownContentType();
        whenIConvertTheFileToProfileFile();
        thenIGetTheExpectedProfileFileWithoutFileType();
    }

    @Test
    void conversionFailedDueToIOExceptio() {
        givenAPdfMultipartFileWithError();
        assertThrows(MultipartFileToProfileFileConverter.AccessErrorException.class,
                this::whenIConvertTheFileToProfileFile);
    }

    private void givenAPdfMultipartFileWithError() {
        this.multipartFile = MockedMultipartFile.builder()
                .originalFilename("file.extension")
                .contentType(FileType.PDF.getMediaType())
                .inputStream(new ByteArrayInputStream(new byte[] { }))
                .size(200)
                .throwErrorOnGetInputStream(true)
                .build();
    }

    private void thenIGetTheExpectedProfileFileWithoutFileType() throws IOException {
        assertEquals(this.multipartFile.getOriginalFilename(), this.profileFileObtained.getFilename());
        assertEquals(this.multipartFile.getInputStream(), this.profileFileObtained.getContentStream());
        assertEquals(this.multipartFile.getSize(), this.profileFileObtained.getSize());
        assertNull(this.profileFileObtained.getFileType());
    }

    private void givenAPdfMultipartFileWithAnUnknownContentType() {
        this.multipartFile = MockedMultipartFile.builder()
                .originalFilename("file.extension")
                .contentType("unknown/type")
                .inputStream(new ByteArrayInputStream(new byte[] { }))
                .size(200)
                .build();
    }

    private void givenAPdfMultipartFile() {
        this.multipartFile = MockedMultipartFile.builder()
                .originalFilename("file.extension")
                .contentType(FileType.PDF.getMediaType())
                .inputStream(new ByteArrayInputStream(new byte[] { }))
                .size(200)
                .build();
    }

    private void whenIConvertTheFileToProfileFile() {
        this.profileFileObtained = this.converter.convert(this.multipartFile);
    }

    private void thenIGetTheExpectedProfileFile() throws IOException {
        assertEquals(this.multipartFile.getOriginalFilename(), this.profileFileObtained.getFilename());
        assertEquals(this.multipartFile.getContentType(), this.profileFileObtained.getFileType().getMediaType());
        assertEquals(this.multipartFile.getInputStream(), this.profileFileObtained.getContentStream());
        assertEquals(this.multipartFile.getSize(), this.profileFileObtained.getSize());
    }

    @Getter
    @NoArgsConstructor
    @SuperBuilder(toBuilder = true)
    private static class MockedMultipartFile implements MultipartFile {
        private String name;
        private String originalFilename;
        private String contentType;
        private boolean empty;
        private long size;
        private byte[] bytes;
        private boolean throwErrorOnGetInputStream;
        @Getter(AccessLevel.NONE)
        private InputStream inputStream;
        @Override
        public InputStream getInputStream() throws IOException {
            if(throwErrorOnGetInputStream) {
                throw new IOException();
            }
            return inputStream;
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException { }
    }
}