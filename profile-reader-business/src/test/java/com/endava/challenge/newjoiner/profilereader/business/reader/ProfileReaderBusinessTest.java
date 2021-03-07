package com.endava.challenge.newjoiner.profilereader.business.reader;

import com.endava.challenge.newjoiner.profilereader.control.converter.Converter;
import com.endava.challenge.newjoiner.profilereader.control.message.MessageQueue;
import com.endava.challenge.newjoiner.profilereader.control.validation.Validation;
import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfileReaderBusinessTest {

    private Converter converter;
    private MessageQueue messageQueue;
    private ProfileReaderBusiness profileReaderBusiness;
    private ProfileFile profileFile;
    private Profile profileReturned;
    private Mono<Profile> result;

    @BeforeEach
    void setupTest() {
        converter = Mockito.mock(Converter.class);
        messageQueue = Mockito.mock(MessageQueue.class);
        profileReaderBusiness = new ProfileReaderBusiness(converter, messageQueue);

        Mockito.doAnswer(this::functionConvertTo)
                .when(converter)
                .convertTo(Mockito.eq(ProfileFile.class),
                        Mockito.eq(Profile.class));

        Mockito.doAnswer(this::functionReturnProfile)
                .when(messageQueue)
                .send(Mockito.any());
    }

    private Object functionReturnProfile(InvocationOnMock invocationOnMock) {
        return invocationOnMock.getArgument(0);
    }

    private Object functionConvertTo(InvocationOnMock invocationOnMock) {
        return (Function<? super ProfileFile, Profile>) (obj) -> this.converter.convert(obj, ProfileFile.class, Profile.class);
    }

    @Test
    void processSuccess() {
        givenAProfileFileWithACompleteProfile();
        givenAConversionForThatProfileFile();
        whenTheProfileFileIsProcessed();
        thenReturnsACompleteProfile();
    }

    private void givenAProfileFileWithACompleteProfile() {
        profileFile = ProfileFile.builder()
                .filename("File.extension")
                .fileType(FileType.PDF)
                .size(1000)
                .contentStream(new ByteArrayInputStream(new byte[]{  }))
                .build();
        profileReturned = Profile.builder()
                .idNumber(BigInteger.ONE)
                .firstName("John")
                .lastName("Smith")
                .stack("Duno")
                .role("Developer")
                .englishLevel("B1")
                .domainExperience("2bdfined")
                .build();
    }

    private void thenProcessFailsDueToMissingField(String field) {
        StepVerifier.create(this.result)
                .verifyErrorSatisfies(e -> {
                    assertEquals(Validation.ValidationException.class, e.getClass());
                    assertTrue(e.getMessage().contains(field));
                });
    }

    private void givenAConversionForThatProfileFile() {
        Mockito.doReturn(this.profileReturned)
                .when(this.converter)
                .convert(Mockito.eq(this.profileFile),
                        Mockito.eq(ProfileFile.class),
                        Mockito.eq(Profile.class));
    }

    private void whenTheProfileFileIsProcessed() {
        this.result = profileReaderBusiness.process(profileFile);
    }

    private void thenReturnsACompleteProfile() {
        StepVerifier.create(this.result)
                .expectNext(this.profileReturned)
                .verifyComplete();
    }

    @Nested
    public class ProfileFileValidations {

        @Test
        void processFailsDueToMissingFilename() {
            givenAProfileFileWithoutAFilename();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("Filename");
        }

        @Test
        void processFailsDueToMissingFileType() {
            givenAProfileFileWithoutAFileType();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("File Type");
        }

        @Test
        void processFailsDueToFileSize() {
            givenAProfileFileWithSizeEqualsZero();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("File is empty");
        }

        @Test
        void processFailsDueToMissingStream() {
            givenAProfileFileWithoutContentStream();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("Stream was not provided");
        }

        private void givenAProfileFileWithoutContentStream() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(1000)
                    // .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();
        }

        private void givenAProfileFileWithSizeEqualsZero() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(0)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();
        }

        private void givenAProfileFileWithoutAFileType() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    // .fileType(FileType.PDF)
                    .size(1000)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();
        }

        private void givenAProfileFileWithoutAFilename() {
            profileFile = ProfileFile.builder()
                    // .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(1000)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();
        }
    }

    @Nested
    public class ProfileValidations {
        @Test
        void processFailsDueToMissingIdNumber() {
            givenAProfileFileWithoutIdNumber();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("ID Number");
        }

        @Test
        void processFailsDueToMissingFirstName() {
            givenAProfileFileWithoutFirstName();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("First Name");
        }

        @Test
        void processFailsDueToMissingLastName() {
            givenAProfileFileWithoutLastName();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("Last Name");
        }

        @Test
        void processFailsDueToMissingStack() {
            givenAProfileFileWithoutStack();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("Stack");
        }

        @Test
        void processFailsDueToMissingRole() {
            givenAProfileFileWithoutRole();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("Role");
        }

        @Test
        void processFailsDueToMissingEnglishLevel() {
            givenAProfileFileWithoutEnglishLevel();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("English Level");
        }

        @Test
        void processFailsDueToMissingDomainExperience() {
            givenAProfileFileWithoutDomainExperience();
            givenAConversionForThatProfileFile();
            whenTheProfileFileIsProcessed();
            thenProcessFailsDueToMissingField("Domain Experience");
        }

        private void givenAProfileFileWithoutDomainExperience() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(1000)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();

            profileReturned = Profile.builder()
                    .idNumber(BigInteger.ONE)
                    .firstName("John")
                    .lastName("Smith")
                    .stack("Duno")
                    .role("Developer")
                    .englishLevel("B1")
                    //.domainExperience("2bdfined")
                    .build();
        }

        private void givenAProfileFileWithoutEnglishLevel() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(1000)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();

            profileReturned = Profile.builder()
                    .idNumber(BigInteger.ONE)
                    .firstName("John")
                    .lastName("Smith")
                    .stack("Duno")
                    .role("Developer")
                    //.englishLevel("B1")
                    .domainExperience("2bdfined")
                    .build();
        }

        private void givenAProfileFileWithoutRole() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(1000)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();

            profileReturned = Profile.builder()
                    .idNumber(BigInteger.ONE)
                    .firstName("John")
                    .lastName("Smith")
                    .stack("Duno")
                    //.role("Developer")
                    .englishLevel("B1")
                    .domainExperience("2bdfined")
                    .build();
        }

        private void givenAProfileFileWithoutStack() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(1000)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();

            profileReturned = Profile.builder()
                    .idNumber(BigInteger.ONE)
                    .firstName("John")
                    .lastName("Smith")
                    //.stack("Duno")
                    .role("Developer")
                    .englishLevel("B1")
                    .domainExperience("2bdfined")
                    .build();
        }

        private void givenAProfileFileWithoutLastName() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(1000)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();

            profileReturned = Profile.builder()
                    .idNumber(BigInteger.ONE)
                    .firstName("John")
                    //.lastName("Smith")
                    .stack("Duno")
                    .role("Developer")
                    .englishLevel("B1")
                    .domainExperience("2bdfined")
                    .build();
        }

        private void givenAProfileFileWithoutFirstName() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(1000)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();

            profileReturned = Profile.builder()
                    .idNumber(BigInteger.ONE)
                    //.firstName("John")
                    .lastName("Smith")
                    .stack("Duno")
                    .role("Developer")
                    .englishLevel("B1")
                    .domainExperience("2bdfined")
                    .build();
        }



        private void givenAProfileFileWithoutIdNumber() {
            profileFile = ProfileFile.builder()
                    .filename("File.extension")
                    .fileType(FileType.PDF)
                    .size(1000)
                    .contentStream(new ByteArrayInputStream(new byte[]{  }))
                    .build();

            profileReturned = Profile.builder()
                    // .idNumber(BigInteger.ONE)
                    .firstName("John")
                    .lastName("Smith")
                    .stack("Duno")
                    .role("Developer")
                    .englishLevel("B1")
                    .domainExperience("2bdfined")
                    .build();
        }
    }
}