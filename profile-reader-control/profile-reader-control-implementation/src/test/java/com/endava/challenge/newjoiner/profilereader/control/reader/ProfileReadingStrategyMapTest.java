package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProfileReadingStrategyMapTest {
    private ProfileReadingStrategyMap profileReadingStrategyMap;
    private ProfileReader mockedReader;
    private ProfileFile profileFileToRead;
    private Profile profileObtained;

    @BeforeEach
    void setupTest() {
        this.profileReadingStrategyMap = new ProfileReadingStrategyMap();
    }

    @Test
    void readSuccessCallingProfileReader() {
        givenAReaderRegistered();
        givenAProfileFileToRead();
        whenReadTheProfileFile();
        ThenAProfileReaderIsCalled();
    }

    @Test
    void readFailsDueToUnregisteredProfileReader() {
        givenAProfileFileToRead();

        assertThrows(ProfileReadingStrategy.ReaderNotFoundException.class,
                this::whenReadTheProfileFile);
    }

    private void givenAProfileFileToRead() {
        this.profileFileToRead = ProfileFile.builder()
                .filename("")
                .fileType(FileType.PDF)
                .build();
    }

    private void whenReadTheProfileFile() {
        this.profileObtained = this.profileReadingStrategyMap.read(this.profileFileToRead);
    }

    private void ThenAProfileReaderIsCalled() {
        Mockito.verify(this.mockedReader, Mockito.times(1))
                .read(Mockito.eq(this.profileFileToRead));
    }

    private void givenAReaderRegistered() {
        this.mockedReader = Mockito.mock(ProfileReader.class);
        this.profileReadingStrategyMap.registerReader(FileType.PDF, this.mockedReader);
    }
}