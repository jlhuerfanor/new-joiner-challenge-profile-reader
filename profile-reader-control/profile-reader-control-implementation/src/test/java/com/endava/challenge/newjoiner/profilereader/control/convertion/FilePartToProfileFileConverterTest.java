package com.endava.challenge.newjoiner.profilereader.control.convertion;

import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FilePartToProfileFileConverterTest {

    private FilePartToProfileFileConverter converter;
    private FilePart multipartFile;
    private Mono<ProfileFile> profileFileObtained;

    @BeforeEach
    void setupTest() {
        converter = new FilePartToProfileFileConverter();
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
        whenIConvertTheFileToProfileFile();
        thenAnErrorIsThrown();
    }

    private void thenAnErrorIsThrown() {
        var verifier = StepVerifier.create(this.profileFileObtained);

        verifier.verifyError();
        verifier.verifyComplete();
    }

    private void givenAPdfMultipartFileWithError() {
        this.multipartFile = MockedMultipartFile.builder()
                ._filename("file.extension")
                ._contentType(MediaType.APPLICATION_PDF)
                ._contentLength(200l)
                .throwErrorOnGetContent(true)
                .build();
    }

    private void thenIGetTheExpectedProfileFileWithoutFileType() throws IOException {
        StepVerifier verifier = StepVerifier.create(this.profileFileObtained)
                .consumeNextWith((profile) -> {
                    assertEquals(this.multipartFile.filename(), profile.getFilename());
                    assertNotNull(profile.getContentStream());
                    assertEquals(this.multipartFile.headers().getContentLength(), profile.getSize());
                    assertNull(profile.getFileType());
                })
                .expectComplete();

    }

    private void givenAPdfMultipartFileWithAnUnknownContentType() {
        this.multipartFile = MockedMultipartFile.builder()
                ._filename("file.extension")
                ._contentType(MediaType.TEXT_XML)
                ._contentLength(200l)
                .build();
    }

    private void givenAPdfMultipartFile() {
        this.multipartFile = MockedMultipartFile.builder()
                ._filename("file.extension")
                ._contentType(MediaType.APPLICATION_PDF)
                ._contentLength(200l)
                .build();
    }

    private void whenIConvertTheFileToProfileFile() {
        this.profileFileObtained = this.converter.convert(this.multipartFile);
    }

    private void thenIGetTheExpectedProfileFile() throws IOException {
        StepVerifier verifier = StepVerifier.create(this.profileFileObtained)
                .consumeNextWith((profile) -> {
                        assertEquals(this.multipartFile.filename(), profile.getFilename());
                        assertNotNull(profile.getContentStream());
                        assertEquals(this.multipartFile.headers().getContentLength(), profile.getSize());
                        assertNotNull(this.multipartFile.headers().getContentType().toString());
                        assertEquals(this.multipartFile.headers().getContentType().toString(), profile.getFileType().getMediaType());
                    })
                .expectComplete();

    }

    @NoArgsConstructor
    @SuperBuilder(toBuilder = true)
    private static class MockedMultipartFile implements FilePart {
        private String _filename;
        private MediaType _contentType;
        private Long _contentLength;
        private boolean throwErrorOnGetContent;

        @Override
        public String filename() {
            return _filename;
        }

        @Override
        public Mono<Void> transferTo(Path dest) {
            return throwErrorOnGetContent ? Mono.error(IOException::new) : Mono.empty();
        }

        @Override
        public String name() {
            return _filename;
        }

        @Override
        public HttpHeaders headers() {
            var header = new HttpHeaders();

            header.setContentType(this._contentType);
            header.setContentLength(this._contentLength);

            return header;
        }

        @Override
        public Flux<DataBuffer> content() {
            return Flux.empty();
        }
    }
}