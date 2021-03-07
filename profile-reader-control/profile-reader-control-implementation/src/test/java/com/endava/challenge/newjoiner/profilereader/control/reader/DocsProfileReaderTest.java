package com.endava.challenge.newjoiner.profilereader.control.reader;

import com.endava.challenge.newjoiner.profilereader.model.domain.FileType;
import com.endava.challenge.newjoiner.profilereader.model.domain.Profile;
import com.endava.challenge.newjoiner.profilereader.model.domain.ProfileFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class DocsProfileReaderTest {

    private ProfileFile profileFile;
    private DocsProfileReader reader;
    private Profile profileObtained;

    @BeforeEach
    void setupTest() {
        reader = new DocsProfileReader();
    }

    @Test
    void readSuccess() {
        givenADocxDocumentWithAProfile();
        whenIReadTheProfileFromTheFile();
        thenGetACompleteProfile();
    }

    @Test
    void readSuccessAnEmptyFile() {
        givenAnEmptyFile();
        whenIReadTheProfileFromTheFile();
        thenGetAnEmptyProfile();
    }

    @Test
    void readFailDueToBadFormat() {
        givenANonDocsFile();
        assertThrows(DocsProfileReader.WordFileAccessException.class, this::whenIReadTheProfileFromTheFile);
    }

    private void givenANonDocsFile() {
        var contentStream = PdfProfileReaderTest.class.getResourceAsStream("/non-pdf-document.txt");
        this.profileFile = ProfileFile.builder()
                .filename("non-pdf-document.txt")
                .fileType(FileType.PDF)
                .size(2000)
                .contentStream(contentStream)
                .build();
    }

    private void givenAnEmptyFile() {
        var contentStream = PdfProfileReaderTest.class.getResourceAsStream("/empty-word-document.docx");
        this.profileFile = ProfileFile.builder()
                .filename("empty-word-document.docx")
                .fileType(FileType.PDF)
                .size(2000)
                .contentStream(contentStream)
                .build();
    }

    private void thenGetAnEmptyProfile() {
        assertNull(profileObtained.getIdNumber());
        assertNull(profileObtained.getFirstName());
        assertNull(profileObtained.getLastName());
        assertNull(profileObtained.getStack());
        assertNull(profileObtained.getRole());
        assertNull(profileObtained.getEnglishLevel());
        assertNull(profileObtained.getDomainExperience());
    }

    private void givenADocxDocumentWithAProfile() {
        var contentStream = PdfProfileReaderTest.class.getResourceAsStream("/profile-template-word-test.docx");
        this.profileFile = ProfileFile.builder()
                .filename("profile-template-word-test.docx")
                .fileType(FileType.WORD)
                .size(2000)
                .contentStream(contentStream)
                .build();
    }

    private void whenIReadTheProfileFromTheFile() {
        this.profileObtained = reader.read(profileFile);
    }

    private void thenGetACompleteProfile() {
        assertEquals(new BigInteger("1023456789"), profileObtained.getIdNumber());
        assertEquals("John", profileObtained.getFirstName());
        assertEquals("Smith", profileObtained.getLastName());
        assertEquals("Duno", profileObtained.getStack());
        assertEquals("Developer", profileObtained.getRole());
        assertEquals("B1", profileObtained.getEnglishLevel());
        assertEquals("2bdfined", profileObtained.getDomainExperience());
    }
}